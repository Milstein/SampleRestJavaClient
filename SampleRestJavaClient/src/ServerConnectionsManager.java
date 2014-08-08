/**
 * Nutanix Java SDK Test Example
 * 
 * @author Vamsi Krishna
 * @author Andre Leibovici
 * @version 1.0
 */
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.TrustManager;

import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.nutanix.prism.services.appliance.configuration.DiskAdministration;
import com.nutanix.prism.services.appliance.configuration.HostAdministration;

/**
 * @author andreleibovici
 * @version $Revision: 1.0 $
 */
public final class ServerConnectionsManager implements Cloneable {

	/**
	 * Field REST_BASE_PATH. (value is
	 * ""https://{0}:9440/PrismGateway/services/rest/v1"")
	 */
	public static final String REST_BASE_PATH = "https://{0}:9440/PrismGateway/services/rest/v1";

	/**
	 * Field DEFAULT_CONNECTION_TIMEOUT.
	 */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000; // 10 secs

	/**
	 * Field DEFAULT_INVOCATION_TIMEOUT.
	 */
	public static final int DEFAULT_INVOCATION_TIMEOUT = 180 * 1000; // 3 min

	/**
	 * Field ServerConnectionsManager.
	 */
	private static ServerConnectionsManager ServerConnectionsManager;

	/**
	 * for testing purpose only, do not use
	 * 
	 * @param mock
	 *            the mock object
	 */
	public static void setServerConnectionManager(ServerConnectionsManager mock) {
		ServerConnectionsManager = mock;
	}

	/**
	 * Method getInstance.
	 * 
	 * @return ServerConnectionsManager
	 */
	public static synchronized ServerConnectionsManager getInstance() {
		if (null == ServerConnectionsManager) {
			ServerConnectionsManager = new ServerConnectionsManager();
		}
		return ServerConnectionsManager;
	}

	/**
	 * Method getRestService.
	 * 
	 * @param server
	 *            String
	 * @param username
	 *            String
	 * @param password
	 *            String
	 * @param classType
	 *            Class<T>
	 * @return T
	 */
	private <T> T getRestService(final String server, final String username,
			final String password, final Class<T> classType) {
		if (null == server) {
			return null;
		}

		final String url = MessageFormat.format(REST_BASE_PATH, server);

		final List<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());

		final T proxy = JAXRSClientFactory.create(url, classType, providers);
		final Client client = WebClient.client(proxy);

		final String encodedCredential = Base64Utility.encode(String.format(
				"%s:%s", username, password).getBytes());
		final String authorizationHeader = String.format("Basic %s",
				encodedCredential);
		client.header("Authorization", authorizationHeader);
		configureHttpConduit(WebClient.getConfig(client).getHttpConduit());

		return proxy;
	}

	/**
	 * Method getDiskAdminRef.
	 * 
	 * @param serverHostPort
	 *            String
	 * @param username
	 *            String
	 * @param password
	 *            String
	 * @return DiskAdministration
	 * @throws Exception
	 */
	public DiskAdministration getDiskAdminRef(final String serverHostPort,
			final String username, final String password) {
		return getRestService(serverHostPort, username, password,
				DiskAdministration.class);
	}

	/**
	 * Method getHostAdminRef.
	 * 
	 * @param serverHostPort
	 *            String
	 * @param username
	 *            String
	 * @param password
	 *            String
	 * @return HostAdministration
	 * @throws Exception
	 */
	public HostAdministration getHostAdminRef(final String serverHostPort,
			final String username, final String password) {
		return getRestService(serverHostPort, username, password,
				HostAdministration.class);
	}

	/**
	 * Method configureHttpConduit.
	 * 
	 * @param conduit
	 *            HTTPConduit
	 */
	private void configureHttpConduit(final HTTPConduit conduit) {
		// TLS Client parameters
		final TLSClientParameters tlsClientParameters = new TLSClientParameters();
		// Disable common name check on certificate.
		tlsClientParameters.setDisableCNCheck(true);
		// Create a trust manager that does not validate any certificate chains.
		tlsClientParameters
				.setTrustManagers(new TrustManager[] { new NullTrustManager() });
		conduit.setTlsClientParameters(tlsClientParameters);

		final HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		httpClientPolicy.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
		httpClientPolicy.setReceiveTimeout(DEFAULT_INVOCATION_TIMEOUT);
		conduit.setClient(httpClientPolicy);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
