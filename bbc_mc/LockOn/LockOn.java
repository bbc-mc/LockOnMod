package net.minecraft.src.bbc_mc.LockOn;

import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import net.minecraft.src.mod_LockOn;

/**
 * 
 * @author bbc_mc
 * @date 2012/10/12
 */
public class LockOn {
    
    private final mod_LockOn mod;
    private EntityLockOn entity;
    private RenderLockOn render;
    
    private MOD_MODE mode = MOD_MODE.MODE_OFF;
    private TARGETING_STATE state = TARGETING_STATE.TARGET_NONE;
    
    // resource
    public String targettingTexturePass = "/bbc_mc/LockOn/texture/target.png";
    
    public enum MOD_MODE {
        MODE_OFF, // no target setted
        MODE_NOCURSOR, // target found, but not set
        MODE_CURSOR // target setted
    }
    
    public enum TARGETING_STATE {
        TARGET_NONE, // no target setted
        TARGET_RECOG, // target found, but not set
        TARGET_SET // target setted
    }
    
    // Difinition of MOD VERSION
    private final String modVersion = "1.2.5-20121012";
    private World lastWorld = null;
    
    public Entity targetEntity;
    public Entity candidateEntity;
    private KeyBinding key_mode;
    private KeyBinding key_LockOn;
    private int offCount;
    
    // ==========
    
    public LockOn(mod_LockOn mod) {
        this.mod = mod;
        this.render = new RenderLockOn( this );
        this.targetEntity = null;
    }
    
    public String getVersion() {
        return modVersion;
    }
    
    public void load() {
        ModLoader.setInGameHook( mod, true, false );
        key_mode = new KeyBinding( "LockOn_Mode", mod_LockOn.cfg_Mode );
        ModLoader.registerKey( mod, key_mode, false );
        
        key_LockOn = new KeyBinding( "LockOn", mod_LockOn.cfg_LockOn );
        ModLoader.registerKey( mod, key_LockOn, false );
        
        ModLoader.addLocalization( "LockOn_Mode", "[LockOn] Change Mode" );
        ModLoader.addLocalization( "LockOn_Mode", "ja_JP", "[LockOn] モード切替" );
        ModLoader.addLocalization( "LockOn", "[LockOn] LockOn" );
        ModLoader.addLocalization( "LockOn", "ja_JP", "[LockOn] ロックオン" );
    }
    
    public boolean onTickInGame(float tick, Minecraft minecraft) {
        this.debugPrint( "onTick: " + this.mode + " " + this.state + "[ target:" + this.targetEntity + "] candiate :" + this.candidateEntity );
        
        if (this.mode == MOD_MODE.MODE_OFF) {
            this.debugPrint( "onTick: MODE_OFF" );
            return true;
        }
        EntityPlayer pl = minecraft.thePlayer;
        // Re-Initialize when world is changed
        if (minecraft.theWorld != lastWorld) {
            entity = new EntityLockOn( minecraft, minecraft.theWorld );
            entity.setPosition( pl.posX, pl.posY - 1.0D, pl.posZ );
            minecraft.theWorld.spawnEntityInWorld( entity );
            lastWorld = minecraft.theWorld;
        }
        
        // 対象Entity選択の更新
        if (this.targetEntity == null) {
            if (this.updateCandidate( minecraft )) {
                this.debugPrint( " update target : " + this.targetEntity );
            }
        } else {
            if (this.targetEntity.isDead) {
                this.candidateEntity = null;
                this.targetEntity = null;
                this.setState( TARGETING_STATE.TARGET_NONE );
                this.showMessage( "[LockOn] Disabled. Target is Dead." );
            } else if (this.targetEntity.getDistanceToEntity( pl ) > mod.range) {
                this.candidateEntity = null;
                this.targetEntity = null;
                this.setState( TARGETING_STATE.TARGET_NONE );
                this.showMessage( "[LockOn] Disabled. Target is Out of range." );
            }
        }
        
        // 視線の更新
        if (this.targetEntity != null) {
            pl.getLookHelper().setLookPosition( this.targetEntity.posX, this.targetEntity.posY + this.targetEntity.getEyeHeight(), this.targetEntity.posZ, 30F, 30F );
            
            double dx = pl.posX - this.targetEntity.posX;
            double dz = pl.posZ - this.targetEntity.posZ;
            Vec3D vec = Vec3D.createVector( pl.posX - this.targetEntity.posX, pl.posY - this.targetEntity.posY, pl.posZ - this.targetEntity.posZ ).normalize();
            double rotationYaw;
            rotationYaw = (float) ((Math.atan2( dz, dx ) * 180D) / Math.PI) - 270F;
            rotationYaw = rotationYaw % 360F;
            
            pl.rotationYaw = (float) rotationYaw;
            pl.getLookHelper().onUpdateLook();
        }
        return true;
    }
    
    public void addRenderer(Map map) {
        map.put( EntityLockOn.class, render );
    }
    
    public void keyboardEvent(KeyBinding keybinding) {
        this.debugPrint( "keyboard : " + keybinding.keyCode + " " + keybinding.keyDescription );
        Minecraft minecraft = ModLoader.getMinecraftInstance();
        if (minecraft.currentScreen != null) {
            return;
        }
        if (keybinding == key_LockOn) {
            if (! this.isModeOFF()) {
                if (this.state == TARGETING_STATE.TARGET_RECOG) {
                    this.targetEntity = this.candidateEntity;
                    this.showMessage( "[LockOn] Set" );
                    this.setState( TARGETING_STATE.TARGET_SET );
                } else if (this.state == TARGETING_STATE.TARGET_SET) {
                    this.targetEntity = null;
                    this.candidateEntity = null;
                    this.setState( TARGETING_STATE.TARGET_NONE );
                    this.showMessage( "[LockOn] Unset" );
                } else { // this.state == TARGETING_STATE.TARGET_NONE
                         // do nothing
                }
            }
        } else if (keybinding == key_mode) {
            if (this.mode == MOD_MODE.MODE_OFF) { // OFF -> NOCURSOR
                this.setMode( MOD_MODE.MODE_NOCURSOR );
                this.debugPrint( "[mode change] OFF -> NOCURSOR" );
                this.showMessage( "[LockOn Mode] No Cursor" );
            } else if (this.mode == MOD_MODE.MODE_NOCURSOR) {
                this.setMode( MOD_MODE.MODE_CURSOR ); // NOCURSOR -> CURSOR
                this.debugPrint( "[mode change] NOCURSOR -> CURSOR" );
                this.showMessage( "[LockOn Mode] Cursor" );
            } else { // CURSOR -> OFF
                this.setMode( MOD_MODE.MODE_OFF );
                this.debugPrint( "[mode change] CURSOR -> OFF" );
                this.showMessage( "[LockOn Mode] Off" );
            }
        }
    }
    
    // =====================================================
    //
    //
    private void showMessage(String str) {
        if (mod.showMessage) {
            ModLoader.getMinecraftInstance().thePlayer.addChatMessage( str );
        }
    }
    
    private boolean updateCandidate(Minecraft minecraft) {
        EntityPlayer pl = minecraft.thePlayer;
        
        this.offCount = 0;
        
        // プレイヤーの視線をゲット
        Vec3D lookAt = pl.getLookVec().normalize();
        lookAt = Vec3D.createVector( (double) mod.range * lookAt.xCoord, (double) mod.range * lookAt.yCoord, (double) mod.range * lookAt.zCoord );
        
        // 視線上で範囲内の一番近い Entity をゲット
        Vec3D vec3d = Vec3D.createVector( pl.posX, pl.posY, pl.posZ );
        Vec3D vec3d1 = vec3d.addVector( lookAt.xCoord, lookAt.yCoord, lookAt.zCoord );
        MovingObjectPosition movingobjectposition = pl.worldObj.rayTraceBlocks_do_do( vec3d, vec3d1, false, true );
        vec3d = Vec3D.createVector( pl.posX, pl.posY, pl.posZ );
        vec3d1 = vec3d.addVector( lookAt.xCoord, lookAt.yCoord, lookAt.zCoord );
        
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.createVector( movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord );
        }
        
        Entity entity = null;
        List list = pl.worldObj.getEntitiesWithinAABBExcludingEntity( pl, pl.boundingBox.expand( (double) mod.range, (double) mod.range, (double) mod.range ) );
        double d = 0.0D;
        for (int l = 0; l < list.size(); l++) {
            Entity entity1 = (Entity) list.get( l );
            
            if (! entity1.canBeCollidedWith()) {
                continue;
            }
            
            float f5 = 0.3F;
            AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand( f5, f5, f5 );
            MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept( vec3d, vec3d1 );
            
            if (movingobjectposition1 == null) {
                continue;
            }
            
            double d1 = vec3d.distanceTo( movingobjectposition1.hitVec );
            
            if (d1 < d || d == 0.0D) {
                entity = entity1;
                d = d1;
            }
        }
        
        if (entity != null) {
            movingobjectposition = new MovingObjectPosition( entity );
        }
        
        if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit.getDistanceToEntity( pl ) <= mod.range) {
            this.candidateEntity = movingobjectposition.entityHit;
            this.setState( TARGETING_STATE.TARGET_RECOG );
            return true;
        } else {
            return false;
        }
    }
    
    // =====================================================
    public void debugPrint(String str) {
        if (mod.debug_mode) {
            System.out.println( str );
        }
    }
    
    public void setMode(MOD_MODE new_mode) {
        this.mode = new_mode;
    }
    
    public MOD_MODE getMode() {
        return this.mode;
    }
    
    public void setState(TARGETING_STATE new_state) {
        this.state = new_state;
    }
    
    public TARGETING_STATE getState() {
        return this.state;
    }
    
    public Entity getShowLockOn() {
        if (this.mode == MOD_MODE.MODE_NOCURSOR) {
            return null;
        } else {
            return this.candidateEntity;
        }
    }
    
    public boolean isModeOFF() {
        return this.mode == MOD_MODE.MODE_OFF;
    }
    
    public boolean isNoCursor() {
        return this.mode == MOD_MODE.MODE_NOCURSOR;
    }
}
