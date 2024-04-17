package ar.edu.itba.pod.server.repositories;

import ar.edu.itba.pod.server.interfaces.repositories.RangeRepository;
import ar.edu.itba.pod.server.models.Counter;
import ar.edu.itba.pod.server.models.Range;
import ar.edu.itba.pod.server.models.Sector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

public class RangeRepositoryImpl implements RangeRepository {
    private int lastId = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(RangeRepositoryImpl.class);


    //synchronized porque se necesita el inicio y el final
    //Usar AtomicInteger no sirve mucho porque:
    //Entra T1 y obtiene el valor anterior (0)
    //Entra T2 y obtiene el valor anterior (0)
    //T1 lo incrementa en 2
    //T2 lo incrementa en 3
    //T1 obtiene (0-2) y T2 obtiene (0-3)
    @Override
    public synchronized Range createRange(final int quantity, final Sector sector) {
        if(quantity<=0){
            throw new IllegalArgumentException();
        }
        LOGGER.info("Create range with quantity {} for the sector {}",quantity,sector);
        List<Counter> counters = IntStream.range(lastId,lastId+quantity)//end is exclusive
                .mapToObj(Counter::new)
                .toList();
        final Range ans =  new Range(lastId,lastId+quantity-1,sector,counters);
        lastId+=quantity;
        return ans;
    }
}
