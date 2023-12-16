package com.insurgencedev.edprisonaddon.listeners;

import com.edwardbelt.edprison.events.EdPrisonAddMultiplierCurrency;
import com.edwardbelt.edprison.events.EdPrisonPossibleEnchantTriggerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.models.booster.GlobalBoosterManager;
import org.insurgencedev.insurgenceboosters.settings.IBoostersPlayerCache;

public final class EdPrisonEventListener implements Listener {

    @EventHandler
    private void onEarn(EdPrisonAddMultiplierCurrency event) {
        String type = event.getCurrency();
        final String NAMESPACE = "EDP_CURRENCY";

        IBoostersPlayerCache.BoosterFindResult pResult = IBoosterAPI.getCache(event.getUUID()).findActiveBooster(type, NAMESPACE);
        if (pResult instanceof IBoostersPlayerCache.BoosterFindResult.Success boosterResult) {
            event.addMultiplier(getMulti(boosterResult.getBooster().getMultiplier()));
        }

        GlobalBoosterManager.BoosterFindResult gResult = IBoosterAPI.getGlobalBoosterManager().findBooster(type, NAMESPACE);
        if (gResult instanceof GlobalBoosterManager.BoosterFindResult.Success boosterResult) {
            event.addMultiplier(getMulti(boosterResult.getBooster().getMultiplier()));
        }
    }

    @EventHandler
    private void onTrigger(EdPrisonPossibleEnchantTriggerEvent event) {
        String type = event.getEnchant();
        final String NAMESPACE = "EDP_ENCHANT";

        IBoostersPlayerCache.BoosterFindResult pResult = IBoosterAPI.getCache(event.getPlayer()).findActiveBooster(type, NAMESPACE);
        if (pResult instanceof IBoostersPlayerCache.BoosterFindResult.Success boosterResult) {
            event.addPercent(event.getPercent() * getMulti(boosterResult.getBooster().getMultiplier()));
        }

        GlobalBoosterManager.BoosterFindResult gResult = IBoosterAPI.getGlobalBoosterManager().findBooster(type, NAMESPACE);
        if (gResult instanceof GlobalBoosterManager.BoosterFindResult.Success boosterResult) {
            event.addPercent(event.getPercent() * getMulti(boosterResult.getBooster().getMultiplier()));
        }
    }


    private double getMulti(double amount) {
        return (amount >= 1) ? amount - 1 : amount;
    }
}
