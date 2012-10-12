package net.minecraft.src;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.bbc_mc.LockOn.LockOn;

/**
 * MOD: LockOn
 * 
 * @author bbc_mc
 * @date 2012/10/12
 */

public class mod_LockOn extends BaseMod {
    
    //
    // Customable Settings
    //
    @MLProp(
            info = "[int range] range in which program search entity by Name.0-256 [default=24]",
            min = 0, max = 256)
    public static int range = 24;
    
    // keybind
    @MLProp(
            info = "mode change key [default=L] http://lwjgl.org/javadoc/constant-values.html#org.lwjgl.input.Keyboard.CHAR_NONE")
    public static int cfg_Mode = org.lwjgl.input.Keyboard.KEY_L; // L key
    @MLProp(
            info = "LockOn key [default=TAB] http://lwjgl.org/javadoc/constant-values.html#org.lwjgl.input.Keyboard.CHAR_NONE")
    public static int cfg_LockOn = org.lwjgl.input.Keyboard.KEY_TAB; // Tab
    
    @MLProp(info = "show mod messages, like mode change [default=true]")
    public static boolean showMessage = true;
    
    // for debug
    public static boolean debug_mode = false;
    //
    private final LockOn mod;
    
    public mod_LockOn() {
        mod = new LockOn( this );
    }
    
    @Override
    public String getVersion() {
        return mod.getVersion();
    }
    
    @Override
    public void load() {
        mod.load();
    }
    
    @Override
    public boolean onTickInGame(float partialTick, Minecraft mc) {
        return mod.onTickInGame( partialTick, mc );
    }
    
    @Override
    public void addRenderer(Map map) {
        mod.addRenderer( map );
    }
    
    @Override
    public void keyboardEvent(KeyBinding keybinding) {
        mod.keyboardEvent( keybinding );
    }
}
