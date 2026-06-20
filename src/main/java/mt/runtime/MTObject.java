package mt.runtime;

import java.util.List;

public interface MTObject {
    MTObject send(String selector, List<MTObject> args);
}
