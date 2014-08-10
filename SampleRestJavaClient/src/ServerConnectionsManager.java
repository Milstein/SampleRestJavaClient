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

import com.nutanix.prism.services.appliance.configuration.ContainerAdministration;
import com.nutanix.prism.services.appliance.configuration.DiskAdministration;
import com.nutanix.prism.services.appliance.configuration.HostAdministration;
import com.nutanix.prism.services.appliance.configuration.ManagementServerAdministration;
import com.nutanix.prism.services.dr.BackupAndDrAdministration;
import com.nutanix.prism.services.vmmanagement.VMAdministration;

/**
 * @author andreleibovici
 * @version $Revision: 1.0 $
 */
public final class ServerConnectionsManager implements Cloneable {

	/**
	 * Field USERNAME
	 */
	private String username = null;

	/**
	 * Field PASSWORD
	 */
	private String password = null;

	/**
	 * Field SERVER
	 */
	private String serverhost = null;

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
	 **/
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
	 * 
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
	 * Method getDiskAdminRef
	 * 
	 * @return DiskAdministration
	 */
	public DiskAdministration getDiskAdminRef() {
		return getRestService(serverhost, username, password,
				DiskAdministration.class);
	}

	/**
	 * Method getHostAdminRef.
	 * 
	 * @return HostAdministration
	 */
	public HostAdministration getHostAdminRef() {
		return getRestService(serverhost, username, password,
				HostAdministration.class);
	}

	/**
	 * Method getContainerAdminRef
	 * 
	 * @return ContainerAdministration
	 */
	public ContainerAdministration getContainerAdminRef() {
		return getRestService(serverhost, username, password,
				ContainerAdministration.class);
	}

	/**
	 * Method getManagementServerAdminRef
	 * 
	 * @return ManagementServerAdministration
	 */
	public ManagementServerAdministration getManagementServerAdminRef() {
		return getRestService(serverhost, username, password,
				ManagementServerAdministration.class);
	}

	/**
	 * 
	 * @return VMAdministration
	 */
	public VMAdministration getVMAdministration() {
		return getRestService(serverhost, username, password,
				VMAdministration.class);
	}

	/**
	 * 
	 * @return BackupAndDrAdministration
	 */
	public BackupAndDrAdministration getBackupAndDrAdministration() {
		return getRestService(serverhost, username, password,
				BackupAndDrAdministration.class);
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

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	/**
	 * Method clone.
	 * 
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Method getUSERNAME.
	 * 
	 * @return String
	 */
	public String getUSERNAME() {
		return username;
	}

	/**
	 * Method setUSERNAME.
	 * 
	 * @param uSERNAME
	 *            String
	 */
	public void setUSERNAME(String uSERNAME) {
		username = uSERNAME;
	}

	/**
	 * Method getPASSWORD.
	 * 
	 * @return String
	 */
	public String getPASSWORD() {
		return password;
	}

	/**
	 * Method setPASSWORD.
	 * 
	 * @param pASSWORD
	 *            String
	 */
	public void setPASSWORD(String pASSWORD) {
		password = pASSWORD;
	}

	/**
	 * Method getSERVER.
	 * 
	 * @return String
	 */
	public String getSERVER() {
		return serverhost;
	}

	/**
	 * Method setSERVER.
	 * 
	 * @param sERVER
	 *            String
	 */
	public void setSERVER(String sERVER) {
		serverhost = sERVER;
	}

}
