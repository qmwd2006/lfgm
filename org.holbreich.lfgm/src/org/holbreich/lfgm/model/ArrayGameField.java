package org.holbreich.lfgm.model;

import org.eclipse.swt.graphics.Point;
import org.holbreich.lfgm.Activator;

/**
 * Represents Game Field
 * 
 * @author Alexander Holbreich (http://alexander.holbreich.org)
 * @version $Rev: 1 $, ${date}$
 */
public class ArrayGameField extends AbstractListenerHolder<IModelChangeListener> implements IGameFieldModel {

	private int			turns;

	private boolean[][]	cells;

	/**
	 * Creates new game Field with with and height.
	 * 
	 * @param width
	 * @param height
	 */
	public ArrayGameField(int width, int height) {
		this.cells = new boolean[height][width];
	}

	@Override
	public int getWidth() {
		return this.cells[0].length;

	}

	@Override
	public int getHeight() {
		return this.cells.length;
	}

	@Override
	public void nextTurn() {
		turns++;

		int neigbours;
		for (int y = 0; y < getHeight(); y++)
		{
			for (int x = 0; x < getWidth(); x++)
			{
				neigbours = getNeighboursOf(x, y);
				if(neigbours==3)
				{
					//give birth
					cells[y][x] =true;
				}
				else if(neigbours>3 || neigbours<2)
				{
					//die...
					cells[y][x] =false;
				}
			}
		}

		Activator.getDefault().logInfo("Turn " + turns);

		fireEvent();
		
	}

	/**
	 * @param x
	 * @param y
	 */
	private int getNeighboursOf(int x, int y) {

		int neighboursCount = 0;
		neighboursCount += getValueAt(x - 1, y - 1);
		neighboursCount += getValueAt(x, y - 1);
		neighboursCount += getValueAt(x + 1, y - 1);
		neighboursCount += getValueAt(x - 1, y);
		neighboursCount += getValueAt(x + 1, y);
		neighboursCount += getValueAt(x - 1, y + 1);
		neighboursCount += getValueAt(x, y + 1);
		neighboursCount += getValueAt(x + 1, y + 1);
		return neighboursCount;
	}

	private int getValueAt(int x, int y) {
		if (y > 0 && y < cells.length && x > 0 && x < cells[y].length)
		{
			return cells[y][x] ? 1 : 0;
		}
		return 0;
	}

	@Override
	public boolean isGameOver() {
		return turns > 1000;

	}

	@Override
	protected void fireOne(IModelChangeListener listener) {
		listener.modelChanged();
	}

	@Override
	public void setAlifeAt(Point modelCoords) {
		assert modelCoords != null;

		this.cells[modelCoords.y][modelCoords.x] = true;
		fireEvent();

	}

	@Override
	public boolean isAlife(int x, int y) {
		return this.cells[y][x];
	}

}
