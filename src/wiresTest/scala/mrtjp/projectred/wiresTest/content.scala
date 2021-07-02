package mrtjp.projectred.wiresTest

import codechicken.lib.util.CrashLock
import codechicken.multipart.api.MultiPartType
import mrtjp.projectred.ProjectRedWiresTest.MOD_ID
import mrtjp.projectred.transmission.DataGen
import mrtjp.projectred.transmission.TransmissionContent.{ITEMS, LOCK, PARTS}
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, ForgeRegistries}

object WiresTestContent {

    private val LOCK = new CrashLock("Already Initialized.")
    private val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID)
    private val PARTS = DeferredRegister.create(classOf[MultiPartType[_]], MOD_ID)

    def register(bus: IEventBus) {
        LOCK.lock()
        ITEMS.register(bus)
        PARTS.register(bus)
        //        bus.register(DataGen)
    }

}