package net.minecraft.src.bbc_mc.LockOn;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityLockOn extends Entity {
    
    public Minecraft minecraft;
    
    public EntityLockOn(Minecraft minecraft, World world) {
        super( world );
        ignoreFrustumCheck = true;
        this.minecraft = minecraft;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
    }
    
    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }
    
    @Override
    public void onUpdate() {
        setPosition( minecraft.thePlayer.posX, minecraft.thePlayer.posY - height, minecraft.thePlayer.posZ );
    }
    
}
