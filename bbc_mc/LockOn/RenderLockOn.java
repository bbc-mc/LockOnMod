package net.minecraft.src.bbc_mc.LockOn;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

public class RenderLockOn extends Render {
    
    private final LockOn mod;
    
    public RenderLockOn(LockOn mod) {
        this.mod = mod;
    }
    
    @Override
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
        if (! mod.isModeOFF()) {
            Entity drawTarget = mod.getShowLockOn();
            if (drawTarget != null) {
                this.drawTargetSight( ModLoader.getMinecraftInstance(), drawTarget );
            }
        }
    }
    
    private void drawTargetSight(Minecraft minecraft, Entity entity) {
        float f1 = 1.6F;
        float f2 = 0.01666667F * f1;
        double d = - (renderManager.field_1222_l - entity.posX);
        double d1 = - (renderManager.field_1221_m - entity.posY);
        double d2 = - (renderManager.field_1220_n - entity.posZ);
        double size;
        float dis = entity.getDistanceToEntity( renderManager.livingPlayer ) - 1.0F;
        
        if (mod.candidateEntity.entityId == entity.entityId) {
            size = 10.0D + dis * 5.0D;
        } else {
            size = 10.0D + dis * 2.5D;
        }
        GL11.glPushMatrix();
        GL11.glTranslated( d, d1 + (entity.height / 1.6D), d2 );
        GL11.glNormal3f( 0.0F, 1.0F, 0.0F );
        GL11.glRotatef( - renderManager.playerViewY, 0.0F, 1.0F, 0.0F );
        if (minecraft.gameSettings.thirdPersonView == 2) {
            GL11.glRotatef( - renderManager.playerViewX, 1.0F, 0.0F, 0.0F );
        } else {
            GL11.glRotatef( renderManager.playerViewX, 1.0F, 0.0F, 0.0F );
        }
        GL11.glScalef( - f2, - f2, f2 );
        GL11.glEnable( GL11.GL_BLEND );
        GL11.glDisable( GL11.GL_LIGHTING );
        GL11.glDisable( GL11.GL_DEPTH_TEST );
        GL11.glDepthMask( false );
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
        GL11.glBindTexture( GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture( mod.targettingTexturePass ) );
        if (mod.candidateEntity.entityId == entity.entityId) {
            GL11.glColor4f( 0x66, 0xff, 0x99, 0xcc );
        } else {
            GL11.glColor4f( 1.0F, 1.0F, 1.0F, 0xcc );
        }
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV( - size, - size, 0.0D, 0.0D, 1.0D );
        tessellator.addVertexWithUV( - size, size, 0.0D, 1.0D, 1.0D );
        tessellator.addVertexWithUV( size, size, 0.0D, 1.0D, 0.0D );
        tessellator.addVertexWithUV( size, - size, 0.0D, 0.0D, 0.0D );
        tessellator.draw();
        GL11.glDepthMask( true );
        GL11.glEnable( GL11.GL_DEPTH_TEST );
        GL11.glEnable( GL11.GL_LIGHTING );
        GL11.glDisable( GL11.GL_BLEND );
        GL11.glPopMatrix();
    }
    
}