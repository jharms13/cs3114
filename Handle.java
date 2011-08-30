/**
 * Hold a reference to some data in memory. This holds this reference as an 
 * offset into the memory pool.
 * 
 * @author Reese Moore
 * @author Tyler Kahn
 * @version 2011.08.30
 */
public class Handle {

	// Private variables
	int offset;
	
	/**
	 * Create a new handle
	 * @param offset
	 */
	public Handle(int offset)
	{
		this.offset = offset;
	}
	
	/**
	 * Get the offset of the handle
	 * @return the offset represented by this handle
	 */
	public int getOffset()
	{
		return this.offset;
	}
}
