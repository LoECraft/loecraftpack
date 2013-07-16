package loecraftpack.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;


/**
 * this task never ends, and can only be stopped by removing it from the active task list
 */
public class EntityAIPlayDead extends EntityAIBase{

	@Override
	public boolean shouldExecute()
	{
		return true;
	}
	
	public void updateTask() 
	{
		System.out.println("It's dead Jim");
	}

}
