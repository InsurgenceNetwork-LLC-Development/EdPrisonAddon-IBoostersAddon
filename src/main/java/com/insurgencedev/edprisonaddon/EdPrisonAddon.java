package com.insurgencedev.edprisonaddon;

import com.insurgencedev.edprisonaddon.listeners.EdPrisonEventListener;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;
import org.insurgencedev.insurgenceboosters.libs.fo.Common;

@IBoostersAddon(name = "EdPrisonAddon", version = "1.0.2", author = "InsurgenceDev", description = "EdPrison Support")
public class EdPrisonAddon extends InsurgenceBoostersAddon {

    @Override
    public void onAddonReloadAblesStart() {
        if (Common.doesPluginExist("EdPrison")) {
            registerEvent(new EdPrisonEventListener());
        }
    }
}
