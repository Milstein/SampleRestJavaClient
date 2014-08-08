/**
 * Nutanix Java SDK Test Example
 * 
 * @author Vamsi Krishna
 * @author Andre Leibovici
 * @version 1.0
 */
import java.util.Collection;
import java.util.List;

import com.nutanix.prism.dto.appliance.configuration.DiskDTO;
import com.nutanix.prism.dto.appliance.configuration.NodeDTO;
import com.nutanix.prism.services.appliance.configuration.DiskAdministration;
import com.nutanix.prism.services.appliance.configuration.HostAdministration;

public class Test implements Cloneable {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		final ServerConnectionsManager connMgr = ServerConnectionsManager
				.getInstance();

		final DiskAdministration ref = connMgr.getDiskAdminRef("10.20.18.10",
				"admin", "admin");
		final Collection<DiskDTO> disks = ref.getDisks(null, null, null)
				.getEntities();
		System.out.println("" + disks);

		final HostAdministration ref1 = connMgr.getHostAdminRef("10.20.18.10",
				"admin", "admin");
		final List<NodeDTO> hosts = ref1.getHosts(null, null, null)
				.getEntities();
		System.out.println("" + hosts);

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
