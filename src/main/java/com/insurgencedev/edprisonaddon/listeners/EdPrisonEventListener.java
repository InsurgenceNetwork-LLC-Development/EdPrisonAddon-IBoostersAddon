package com.insurgencedev.edprisonaddon.listeners;

import com.edwardbelt.edprison.events.EdPrisonAddMultiplierCurrency;
import com.edwardbelt.edprison.events.EdPrisonPossibleEnchantTriggerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.data.BoosterFindResult;

public final class EdPrisonEventListener implements Listener {

    @EventHandler
    private void onEarn(EdPrisonAddMultiplierCurrency event) {
        final String TYPE = event.getCurrency();
        final String NAMESPACE = "EDP_CURRENCY";

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getUUID()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            event.addMultiplier(boosterResult.getBoosterData().getMultiplier());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            event.addMultiplier(globalBooster.getMultiplier());
            return null;
        }, () -> null);
    }

    @EventHandler
    private void onTrigger(EdPrisonPossibleEnchantTriggerEvent event) {
        final String TYPE = event.getEnchant();
        final String NAMESPACE = "EDP_ENCHANT";

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(event.getPlayer()).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            event.addPercent(event.getPercent() * boosterResult.getBoosterData().getMultiplier());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            event.addPercent(event.getPercent() * globalBooster.getMultiplier());
            return null;
        }, () -> null);
    }
}
