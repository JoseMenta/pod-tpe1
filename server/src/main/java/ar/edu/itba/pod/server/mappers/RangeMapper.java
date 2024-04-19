package ar.edu.itba.pod.server.mappers;

import ar.edu.itba.pod.grpc.commons.RangeMessage;
import ar.edu.itba.pod.server.models.Range;

public class RangeMapper {

    public static RangeMessage mapToRangeMessage(Range range) {
        return RangeMessage.newBuilder()
                .setEnd(range.getEnd())
                .setStart(range.getStart())
                .build();
    }
}
