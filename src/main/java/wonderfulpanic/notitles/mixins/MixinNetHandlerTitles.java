/*
 * Copyright (C) 2024 WonderfulPanic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package wonderfulpanic.notitles.mixins;

import java.util.regex.Pattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerTitles implements INetHandlerPlayClient{
	private static final Pattern whitespaceRemovePattern2=Pattern.compile("\\s{2,}");
	private static final Pattern whitespaceRemovePattern=Pattern.compile("\\s");
	private String lastTitle="",lastSubtitle="";
	private long lastTitleTime=System.currentTimeMillis();
	private long lastSubtitleTime=System.currentTimeMillis();
	@Shadow
	private Minecraft gameController;
	@Override
	@Overwrite
	public void handleTitle(SPacketTitle packet){
		PacketThreadUtil.checkThreadAndEnqueue(packet,this,gameController);
		ITextComponent message=packet.getMessage();
		String text=message==null?"":packet.getMessage().getFormattedText();
		SPacketTitle.Type type=packet.getType();
		if(type==SPacketTitle.Type.ACTIONBAR){
			gameController.ingameGUI.setOverlayMessage(text,false);
			return;
		}
		text=whitespaceRemovePattern2.matcher(packet.getMessage().getFormattedText()).replaceAll("  ");
		if(text==null)
			return;
		String trim=whitespaceRemovePattern.matcher(TextFormatting.getTextWithoutFormattingCodes(text)).replaceAll("");
		if(trim==null||trim.length()==0)
			return;
		hasSymbols:{
			for(char c:trim.toCharArray())
				if(Character.isAlphabetic(c)||Character.isDigit(c))
					break hasSymbols;
			return;
		}
		long time=System.currentTimeMillis();
		switch(type){
			case TITLE:
				if(lastTitle.equalsIgnoreCase(trim)||lastTitleTime>time)
					return;
				gameController.player.sendMessage(new TextComponentString("Title: "+text));
				lastTitle=trim;
				lastTitleTime=time+900L;
				break;
			case SUBTITLE:
				if(lastSubtitle.equalsIgnoreCase(trim)||lastSubtitleTime>time)
					return;
				gameController.player.sendMessage(new TextComponentString("Subtitle: "+text));
				lastSubtitle=trim;
				lastSubtitleTime=time+900L;
				break;
			default:
				break;
		}
	}
}
