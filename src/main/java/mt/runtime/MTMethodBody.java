package mt.runtime;

@FunctionalInterface
public interface MTMethodBody {

    MTObject execute(
            MTObject receiver,
            MTArray  arguments,
            MTScope  scope);

}
