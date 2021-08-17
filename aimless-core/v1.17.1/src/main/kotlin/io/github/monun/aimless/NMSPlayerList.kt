package io.github.monun.aimless

import io.github.monun.aimless.plugin.List
import org.bukkit.entity.Player
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket
import net.minecraft.server.level.ServerPlayer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer

class NMSPlayerList : List {
    override fun updatePlayerList() {
        for(player in Bukkit.getOnlinePlayers()) {
            val list = arrayListOf<ServerPlayer>()
            Bukkit.getOfflinePlayers().forEach { p ->
                list += (p.player as CraftPlayer).handle
            }
            (player as CraftPlayer).handle.connection
                .send(ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, list))
        }
    }
}