package co.rsk.net.light.state;

import co.rsk.net.light.LightPeer;
import co.rsk.net.light.LightSyncProcessor;
import org.ethereum.core.BlockHeader;

import java.util.ArrayList;
import java.util.List;

public class FetchRoundSyncState implements LightSyncState {
    private final LightPeer lightPeer;
    private final List<BlockHeader> sparseHeaders;
    private final long targetNumber;
    private final LightSyncProcessor lightSyncProcessor;
    private int maxAmountOfHeaders;
    private byte[] startBlockHash;

    public FetchRoundSyncState(LightPeer lightPeer, List<BlockHeader> sparseHeaders, long targetNumber, LightSyncProcessor lightSyncProcessor) {
        this.lightPeer = lightPeer;
        this.sparseHeaders = new ArrayList<>(sparseHeaders);
        this.targetNumber = targetNumber;
        this.lightSyncProcessor = lightSyncProcessor;
    }

    @Override
    public void sync() {
        BlockHeader high = sparseHeaders.get(1);
        BlockHeader low = sparseHeaders.get(0);
        maxAmountOfHeaders = Math.toIntExact(high.getNumber() - low.getNumber() - 1);
        startBlockHash = high.getParentHash().getBytes();
        lightSyncProcessor.sendBlockHeadersByHashMessage(lightPeer, startBlockHash, maxAmountOfHeaders, 0, true);
    }

    @Override
    public void newBlockHeaders(LightPeer lightPeer, List<BlockHeader> blockHeaders) {
        if (lightSyncProcessor.isCorrect(blockHeaders, maxAmountOfHeaders,  startBlockHash, 0, true)) {
            return;
        }


    }


}
