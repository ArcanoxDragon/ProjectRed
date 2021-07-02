package mrtjp.projectred

import mrtjp.projectred.ProjectRedWiresTest.proxy
import mrtjp.projectred.api.ProjectRedAPI
import mrtjp.projectred.core.WirePropagator
import mrtjp.projectred.wiresTest.{WiresTestContent, WiresTestProxy, WiresTestProxyClient}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.event.lifecycle.{FMLClientSetupEvent, FMLCommonSetupEvent, FMLDedicatedServerSetupEvent, FMLLoadCompleteEvent}
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent
import net.minecraftforge.scorge.lang.ScorgeModLoadingContext

object ProjectRedWiresTest {
    final var MOD_ID = "projectred-wires-test"
    final var proxy: WiresTestProxy = DistExecutor.safeRunForDist(
        () => () => new WiresTestProxyClient().asInstanceOf[WiresTestProxy],
        () => () => new WiresTestProxy())
}

class ProjectRedWiresTest {
    proxy.construct()
    ScorgeModLoadingContext.get.getModEventBus.register(this)
    WiresTestContent.register(ScorgeModLoadingContext.get.getModEventBus)
    MinecraftForge.EVENT_BUS.addListener(serverStarting)

    @SubscribeEvent
    def onCommonSetup(event: FMLCommonSetupEvent) {
        proxy.commonSetup(event)
    }

    @SubscribeEvent
    def onClientSetup(event: FMLClientSetupEvent) {
        proxy.clientSetup(event)
    }

    @SubscribeEvent
    def onServerSetup(event: FMLDedicatedServerSetupEvent) {
        proxy.serverSetup(event)
    }

    @SubscribeEvent
    def onLoadComplete(event: FMLLoadCompleteEvent) {
        proxy.loadComplete(event)
    }

    def serverStarting(event: FMLServerAboutToStartEvent) {
        WirePropagator.reset()
    }
}
