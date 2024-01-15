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

package wonderfulpanic.notitles;

import java.util.Map;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

@Name("NoTitlesCoreMod")
@MCVersion("1.12.2")
public class CoreMod implements IFMLLoadingPlugin{
	@Override
	public String[]getASMTransformerClass(){
		return new String[0];
	}
	@Override
	public String getModContainerClass(){
		return null;
	}
	@Override
	public String getSetupClass(){
		return null;
	}
	@Override
	public void injectData(Map<String,Object>data){
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.notitles.json");
		MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
	}
	@Override
	public String getAccessTransformerClass(){
		return null;
	}
}