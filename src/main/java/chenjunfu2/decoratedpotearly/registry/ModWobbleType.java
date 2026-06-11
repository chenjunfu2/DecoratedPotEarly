package chenjunfu2.decoratedpotearly.registry;

public enum ModWobbleType
{
	POSITIVE(7),
	NEGATIVE(10);
	
	public final int lengthInTicks;
	
	private ModWobbleType(final int lengthInTicks) {
		this.lengthInTicks = lengthInTicks;
	}
}
