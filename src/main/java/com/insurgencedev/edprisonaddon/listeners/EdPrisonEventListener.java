package com.insurgencedev.edprisonaddon.listeners;

import com.edwardbelt.edprison.events.EdPrisonAddMultiplierCurrency;
import com.edwardbelt.edprison.events.EdPrisonPossibleEnchantTriggerEvent;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.insurgencedev.insurgenceboosters.api.IBoosterAPI;
import org.insurgencedev.insurgenceboosters.data.BoosterFindResult;
import org.insurgencedev.insurgenceboosters.data.PermanentBoosterData;

import java.util.Optional;
import java.util.UUID;

public final class EdPrisonEventListener implements Listener {

    @EventHandler
    private void onEarn(EdPrisonAddMultiplierCurrency event) {
        final String TYPE = event.getCurrency();
        final String NAMESPACE = "EDP_CURRENCY";
        UUID uuid = event.getUUID();
        AtomicDouble totalMulti = new AtomicDouble(getPermMulti(uuid, TYPE, NAMESPACE) + getGlobalPermMulti(TYPE, NAMESPACE));

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(uuid).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti.getAndAdd(boosterResult.getBoosterData().getMultiplier());
            event.addMultiplier(totalMulti.get());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti.getAndAdd(globalBooster.getMultiplier());
            event.addMultiplier(totalMulti.get());
            return null;
        }, () -> null);
    }

    @EventHandler
    private void onTrigger(EdPrisonPossibleEnchantTriggerEvent event) {
        final String TYPE = event.getEnchant();
        final String NAMESPACE = "EDP_ENCHANT";
        Player player = event.getPlayer();
        AtomicDouble totalMulti = new AtomicDouble(getPermMulti(player.getUniqueId(), TYPE, NAMESPACE) + getGlobalPermMulti(TYPE, NAMESPACE));

        BoosterFindResult pResult = IBoosterAPI.INSTANCE.getCache(player).getBoosterDataManager().findActiveBooster(TYPE, NAMESPACE);
        if (pResult instanceof BoosterFindResult.Success boosterResult) {
            totalMulti.getAndAdd(boosterResult.getBoosterData().getMultiplier());
            event.addPercent(event.getPercent() * totalMulti.get());
        }

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findGlobalBooster(TYPE, NAMESPACE, globalBooster -> {
            totalMulti.getAndAdd(globalBooster.getMultiplier());
            event.addPercent(event.getPercent() * totalMulti.get());
            return null;
        }, () -> null);
    }

    private double getPermMulti(UUID uuid, String type, String namespace) {
        Optional<PermanentBoosterData> foundMulti = Optional.ofNullable(IBoosterAPI.INSTANCE.getCache(uuid).getPermanentBoosts().getPermanentBooster(type, namespace));
        return foundMulti.map(PermanentBoosterData::getMulti).orElse(0d);
    }

    private double getGlobalPermMulti(String type, String namespace) {
        AtomicDouble multi = new AtomicDouble(0d);

        IBoosterAPI.INSTANCE.getGlobalBoosterManager().findPermanentBooster(type, namespace, data -> {
            multi.set(data.getMulti());
            return null;
        }, () -> null);

        return multi.get();
    }
}
