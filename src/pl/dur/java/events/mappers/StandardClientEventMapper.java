/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.dur.java.events.mappers;

import java.util.List;
import pl.dur.java.lowLevelActions.NewPortAction;

/**
 *
 * @author Dur
 */
public class StandardClientEventMapper extends EventMapper
{

	public StandardClientEventMapper()
	{
		super();
		fulfillMap( null );
	}
	
	

	@Override
	protected void fulfillMap( List<EventMapper> eventMappersList )
	{
		getActionMapper().put( "NP", new NewPortAction());
	}
	
}
