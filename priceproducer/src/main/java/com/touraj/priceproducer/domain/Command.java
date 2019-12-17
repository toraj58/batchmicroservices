package com.touraj.priceproducer.domain;

import com.touraj.priceproducer.common.CommandType;
import java.util.UUID;

/**
 * Created by toraj on 08/25/2018.
 */
public class Command {

    CommandType commandType;
    UUID batchID;
    ChunkOfData chunkOfData;

    public ChunkOfData getChunkOfData() {
        return chunkOfData;
    }

    public void setChunkOfData(ChunkOfData chunkOfData) {
        this.chunkOfData = chunkOfData;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public UUID getBatchID() {
        return batchID;
    }

    public void setBatchID(UUID batchID) {
        this.batchID = batchID;
    }
}
