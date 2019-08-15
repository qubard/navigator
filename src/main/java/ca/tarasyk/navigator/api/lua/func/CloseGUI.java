package ca.tarasyk.navigator.api.lua.func;

import ca.tarasyk.navigator.NavigatorProvider;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class CloseGUI extends OneArgFunction {
    /**
     * @param arg The time in milliseconds to wait before closing
     * @return
     */
    @Override
    public LuaValue call(LuaValue arg) {
        int waitTimeMs = arg.checkint();
        long start = System.currentTimeMillis();
        while (NavigatorProvider.getMinecraft().currentScreen == null || (System.currentTimeMillis() - start < waitTimeMs)) { }
        NavigatorProvider.getMinecraft().player.closeScreen();
        NavigatorProvider.getMinecraft().displayGuiScreen(null);
        return null;
    }
}
