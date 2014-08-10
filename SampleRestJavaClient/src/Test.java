/**
 * Nutanix Java SDK Test Example
 * 
 * @author Vamsi Krishna
 * @author Andre Leibovici
 * @version 1.0
 */
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.nutanix.prism.dto.appliance.configuration.ContainerDTO;
import com.nutanix.prism.dto.appliance.configuration.DiskDTO;
import com.nutanix.prism.dto.appliance.configuration.ManagementServerDTO;
import com.nutanix.prism.dto.appliance.configuration.NodeDTO;
import com.nutanix.prism.dto.dr.ProtectionDomainDTO;
import com.nutanix.prism.dto.stats.VMDTO;
import com.nutanix.prism.exception.appliance.configuration.ContainerAdministrationException;
import com.nutanix.prism.exception.appliance.configuration.DiskAdministrationException;
import com.nutanix.prism.exception.appliance.configuration.HostAdministrationException;
import com.nutanix.prism.exception.appliance.configuration.ManagementServerAdministrationException;
import com.nutanix.prism.exception.dr.BackupAndDrAdministrationException;
import com.nutanix.prism.exception.vmmanagement.VMAdministrationException;
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
public class Test implements Cloneable {

	/**
	 * @param args
	 * @throws VMAdministrationException
	 * @throws DiskAdministrationException
	 * @throws HostAdministrationException
	 * @throws ContainerAdministrationException
	 * @throws BackupAndDrAdministrationException
	 * @throws ManagementServerAdministrationException
	 * @throws Exception
	 **/
	public static void main(String[] args) throws VMAdministrationException,
			DiskAdministrationException, HostAdministrationException,
			ContainerAdministrationException,
			BackupAndDrAdministrationException,
			ManagementServerAdministrationException {

		// Stablish connection with PRISM API
		final ServerConnectionsManager connMgr = ServerConnectionsManager
				.getInstance();
		connMgr.setUSERNAME("admin");
		connMgr.setPASSWORD("admin");
		connMgr.setSERVER("10.20.18.10");

		// Retrieve list of virtual machines and properties
		System.out.println();
		System.out.println("Virtual Machines");
		final VMAdministration ref4 = connMgr.getVMAdministration();
		final List<VMDTO> vms = ref4.getVMs(null, null, null).getEntities();
		final Iterator<VMDTO> iteratorVms = vms.iterator();
		while (iteratorVms.hasNext()) {
			System.out.println(iteratorVms.next());
		}

		// Retrieve list of disks and properties
		System.out.println();
		System.out.println("Disks");
		final DiskAdministration ref = connMgr.getDiskAdminRef();
		final Collection<DiskDTO> disks = ref.getDisks(null, null, null)
				.getEntities();
		final Iterator<DiskDTO> iteratorDisks = disks.iterator();
		while (iteratorDisks.hasNext()) {
			System.out.println(iteratorDisks.next());
		}

		// Retrieve list of hosts and properties
		System.out.println();
		System.out.println("Hosts");
		final HostAdministration ref1 = connMgr.getHostAdminRef();
		final List<NodeDTO> hosts = ref1.getHosts(null, null, null)
				.getEntities();
		final Iterator<NodeDTO> iteratorHosts = hosts.iterator();
		while (iteratorHosts.hasNext()) {
			System.out.println(iteratorHosts.next());
		}

		// Retrieve list of containers and properties
		System.out.println();
		System.out.println("Containers");
		final ContainerAdministration ref2 = connMgr.getContainerAdminRef();
		final List<ContainerDTO> containers = ref2.getContainers(null, null,
				null).getEntities();
		final Iterator<ContainerDTO> iteratorContainers = containers.iterator();
		while (iteratorContainers.hasNext()) {
			System.out.println(iteratorContainers.next());
		}

		// Retrieve list of management servers and properties
		System.out.println();
		System.out.println("Management Servers");
		final ManagementServerAdministration ref3 = connMgr
				.getManagementServerAdminRef();
		final List<ManagementServerDTO> managementServers = ref3
				.getManagementServers(null);
		final Iterator<ManagementServerDTO> iteratorManagementServers = managementServers
				.iterator();
		while (iteratorManagementServers.hasNext()) {
			System.out.println(iteratorManagementServers.next());
		}

		// Retrieve list of protection domains and protected vms
		System.out.println();
		System.out.println("Protection Domains");
		final BackupAndDrAdministration ref5 = connMgr
				.getBackupAndDrAdministration();
		final List<ProtectionDomainDTO> backupAndDrAdministration = ref5
				.getProtectionDomains(null, true);
		final Iterator<ProtectionDomainDTO> iteratorBackupAndDrAdministration = backupAndDrAdministration
				.iterator();
		while (iteratorBackupAndDrAdministration.hasNext()) {
			System.out.println(iteratorBackupAndDrAdministration.next());
		}
	}

	/**
	 * Method toString.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
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
}
