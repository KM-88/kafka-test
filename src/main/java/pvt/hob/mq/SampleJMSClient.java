package pvt.hob.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 * Details of JMS resources that are used by enterprise applications are defined
 * to WebSphere® Application Server and bound into the JNDI namespace by the
 * WebSphere Application Server administrative support.
 * 
 * Web URL :
 * https://www.ibm.com/support/knowledgecenter/SSEQTP_8.5.5/com.ibm.websphere.base.iseries.doc/ae/tmj_devap.html
 * 
 * @author kranti
 *
 */
public class SampleJMSClient {

	private static String qcfName = "java:comp/env/jms/ConnectionFactory";
	private static String qnameIn = "java:comp/env/jms/Q1";
	private static String qnameOut = "java:comp/env/jms/Q2";

	private static QueueSession session = null;
	private static QueueConnection connection = null;
	private static Context ctx = null;

	private static QueueConnectionFactory qcf = null;
	private static Queue inQueue = null;
	private static Queue outQueue = null;
	private static QueueSender queueSender = null;
	private static QueueReceiver queueReceiver = null;
	private static Topic topic = null;

	public static void main(String[] args) throws JMSException, Exception

	{
		try {

			getSession();

			// Create MessageProducers to create messages
			// For point-to-point the MessageProducer is a QueueSender that is created by
			// passing an output queue object (retrieved earlier) into the createSender
			// method on the session. A QueueSender is usually created for a specific queue,
			// so that all messages sent by that sender are sent to the same destination.
			queueSender = session.createSender(inQueue);

			String message = "";
			String selector = sendMessage(message);
			String replyString = checkResponse(selector);
			System.out.println("Server Response : " + replyString);
			createTopicPublisher();
			closeConnection();
		} catch (JMSException je) {
			// Handling errors
			// Any JMS runtime errors are reported by exceptions. The majority of methods in
			// JMS throw JMSExceptions to indicate errors. It is good programming practice
			// to catch these exceptions and display them on a suitable output.
			// Unlike normal Java™ exceptions, a JMSException can contain another exception
			// embedded in it. The implementation of JMSException does not include the
			// embedded exception in the output of its toString()method. Therefore, you must
			// check explicitly for an embedded exception and print it out, as shown in the
			// following example:
			System.out.println("JMS failed with " + je);
			Exception le = je.getLinkedException();
			if (le != null) {
				System.out.println("linked exception " + le);
			}
		}

	}

	/**
	 * If the application needs to create many short-lived JMS objects at the
	 * Session level or lower, it is important to close all the JMS resources used.
	 * To do this, you call the close() method on the various classes
	 * (QueueConnection, QueueSession, QueueSender, and QueueReceiver) when the
	 * resources are no longer required.
	 * 
	 * @throws JMSException
	 */
	private static void closeConnection() throws JMSException {
		queueReceiver.close();
		queueSender.close();
		session.close();
		session = null;
		connection.close();
		connection = null;
	}

	private static void createTopicPublisher() throws JMSException {
		Topic topic;
		// Publishing and subscribing messages.
		// To use publish/subscribe support instead of point-to-point messaging, the
		// general client actions are the same; for example, to create a session and
		// connection. The exceptions are that topic resources are used instead of queue
		// resources (such as TopicPublisher instead of QueueSender), as shown in the
		// following example to publish a message:
		// Creating a TopicPublisher
		topic = session.createTopic("");
		// TopicPublisher pub = session.
		// pub.publish(outMessage);
		// Closing TopicPublisher
		// pub.close();
		// Closing down.
	}

	private static String checkResponse(String selector) throws JMSException {
		String replyString = null;
		// Create a MessageReceiver to receive messages.
		// For point-to-point the MessageReceiver is a QueueReceiver that is created by
		// passing an input queue object (retrieved earlier) and the message selector
		// into the createReceiver method on the session.
		queueReceiver = session.createReceiver(outQueue, selector);
		// Retrieve the reply message.
		// To retrieve a reply message, the receive method on the QueueReceiver is used:
		Message inMessage = queueReceiver.receive(2000);
		// The parameter in the receive call is a timeout in milliseconds.
		// This parameter defines how long the method should wait if there is no message
		// available immediately. If you omit this parameter, the call blocks
		// indefinitely. If you do not want any delay, use the receiveNoWait()method. In
		// this example, the receive call returns when the message arrives, or after
		// 2000ms, whichever is sooner.

		// Act on the message received.
		//// When a message is received, you can act on it as needed by the business
		// logic of the client. Some general JMS actions are to check that the message
		// is of the correct type and extract the content of the message. To extract the
		// content from the body of the message, cast from the generic Message class
		// (which is the declared return type of the receive methods) to the more
		// specific subclass, such as TextMessage. It is good practice always to test
		// the message class before casting, so that unexpected errors can be handled
		// gracefully.
		// In this example, the instanceof operator is used to check that the message
		// received is of the TextMessage type. The message content is then extracted by
		// casting to the TextMessage subclass.
		if (inMessage instanceof TextMessage) {
			replyString = ((TextMessage) inMessage).getText();
		}
		return replyString;
	}

	private static String sendMessage(String message) throws JMSException {
		// Create the message.
		// Use the session to create an empty message and add the data passed.
		// JMS provides several message types, each of which embodies some knowledge of
		// its content. To avoid referencing the vendor-specific class names for the
		// message types, methods are provided on the Session object for message
		// creation.
		// In this example, a text message is created from the outString property, which
		// could be provided as an input parameter on invocation of the client program
		// or constructed in some other way:
		TextMessage outMessage = session.createTextMessage(message);
		// Send the message.
		// To send the message, the message is passed to the send method on the
		// QueueSender:
		queueSender.send(outMessage);

		// Receive replies
		// Create a correlation ID to link the message sent with any replies.
		// In this example, the client receives reply messages that are related to the
		// message that it has sent, by using a provider-specific message ID in a
		// JMSCorrelationID.
		String messageID = outMessage.getJMSMessageID();
		// The correlation ID is then used in a message selector, to select only
		// messages that have that ID:
		return "JMSCorrelationID = '" + messageID + "'";
	}

	private static Session getSession() throws NamingException, JMSException {
		qcf = (QueueConnectionFactory) ctx.lookup(qcfName);
		inQueue = (Queue) ctx.lookup(qnameIn);
		outQueue = (Queue) ctx.lookup(qnameOut);
		connection = qcf.createQueueConnection();
		connection.start();
		// Create a session for sending and receiving messages
		boolean transacted = false;
		session = connection.createQueueSession(transacted, Session.AUTO_ACKNOWLEDGE);
		return session;
	}
}
