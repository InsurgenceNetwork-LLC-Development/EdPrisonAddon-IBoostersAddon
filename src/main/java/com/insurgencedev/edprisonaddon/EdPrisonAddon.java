package com.insurgencedev.edprisonaddon;

import com.insurgencedev.edprisonaddon.listeners.EdPrisonEventListener;
import org.insurgencedev.insurgenceboosters.api.addon.IBoostersAddon;
import org.insurgencedev.insurgenceboosters.api.addon.InsurgenceBoostersAddon;

@IBoostersAddon(name = "EdPrisonAddon", version = "1.0.2", author = "InsurgenceDev", description = "EdPrison Support")
public class EdPrisonAddon extends InsurgenceBoostersAddon {

    @Override
    public void onAddonReloadAblesStart() {
        registerEvent(new EdPrisonEventListener());
    }
}
