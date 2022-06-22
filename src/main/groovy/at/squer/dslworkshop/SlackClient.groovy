package at.squer.dslworkshop

import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.request.chat.ChatPostMessageRequest
import com.slack.api.methods.request.conversations.ConversationsHistoryRequest
import com.slack.api.methods.request.users.UsersListRequest

class SlackClient {

    /**
     * Use as Singleton via this instance
     */
    static SlackClient instance = new SlackClient()

    private MethodsClient methodsClient

    private long startingTimestamp = (long) (new Date().time / 1000)

    private Map<String, String> userNameToChannelIds = [:]
    private Map<String, String> listenerChannelIds = [:]
    private Set<String> readMessageTimestamps = new HashSet<>()
    private Map<String, Closure> messageHandlers = [:]

    private SlackClient() {
        methodsClient = Slack.getInstance().methods("xoxb-2812189956647-3700710743730-bcHTQSE8krrrUmwFKxNSaalq");
        def response = methodsClient.usersList(UsersListRequest.builder().build())
        response.members.forEach(member -> userNameToChannelIds[member.realName] = member.id)
    }

    /**
     * Sends a message to a group of private channels of users identified by their real names.
     *
     * @param message Message content.
     * @param userNames List of real user names.
     */
    void sendMessage(String message, List<String> userNames) {

        userNames.forEach { userName ->
            def channelId = userNameToChannelIds[userName]
            if (channelId != null) {
                def response = methodsClient.chatPostMessage(ChatPostMessageRequest.builder()
                        .channel(channelId)
                        .text(message)
                        .build())
                if (response.ok) {
                    readMessageTimestamps.add(response.ts)
                    listenerChannelIds[userName] = response.channel
                }
            } else {
                System.err.println("Cannot send message to $userName. Unknown!?")
            }
        }
    }

    /**
     * Fetches messages from private channel of a single user identified by his real name.
     *
     * @param userName Real user name.
     * @return List of message contents.
     */
    List<String> fetchUnreadMessages(String userName) {

        def channelId = listenerChannelIds[userName]

        if (channelId != null) {
            def response = methodsClient.conversationsHistory(ConversationsHistoryRequest.builder()
                    .oldest(startingTimestamp as String)
                    .channel(channelId)
                    .build())
            if (response.ok) {
                def messageTexts = response.messages
                        .grep { msg -> !readMessageTimestamps.contains(msg.ts) }
                        .collect { it.text }
                readMessageTimestamps.addAll(response.messages.collect { it.ts })
                return messageTexts.reverse()
            } else {
                System.err.println("Cannot fetch messages from $userName")
            }
        } else {
            System.err.println("Cannot fetch messages from $userName (yet). Please send an initial message first.")
        }
        return []
    }

    /**
     * Starts listening to messages in private channel of a single user identified by his real name.
     *
     * @param userName Real user name.
     * @param handler Handler closure
     */
    void registerMessageListener(String userName, Closure handler) {

        messageHandlers[userName] = handler
        if (handler != null) {
            Thread.start {
                do {
                    fetchUnreadMessages(userName).forEach { msgText -> handler(msgText) }
                    Thread.sleep(1000)
                } while (messageHandlers[userName] == handler)
            }
        }
    }
}
