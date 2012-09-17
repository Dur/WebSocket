package pl.dur.java.components.register;

/**
 *
 * @author Dur
 */
public class ClientsComponentsDictionary
{
	public enum StandardClientsCompomnents
	{
		SOCKET_ADMIN,
		QUEUE_SIZE,
		ACTIONS_QUEUE,
		OUTPUT_SENDER,
		DISPATCHER,
		CONNECTION_RECEIVER;
	}
		private ClientsComponentsDictionary component;

		public void setComponentName( ClientsComponentsDictionary newComponent )
		{
			this.component = newComponent;
		}

		public String getComponentName()
		{
			return component.toString();
		}
	}
