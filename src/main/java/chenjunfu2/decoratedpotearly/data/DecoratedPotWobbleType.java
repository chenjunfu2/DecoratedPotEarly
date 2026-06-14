package chenjunfu2.decoratedpotearly.data;

public enum DecoratedPotWobbleType
{
	POSITIVE(7),
	NEGATIVE(10);
	
	public final int lengthInTicks;
	
	DecoratedPotWobbleType(final int lengthInTicks) {
		this.lengthInTicks = lengthInTicks;
	}
}
