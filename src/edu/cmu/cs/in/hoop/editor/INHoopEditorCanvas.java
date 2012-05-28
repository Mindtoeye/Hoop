/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation, either version 3 of the 
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.cmu.cs.in.hoop.editor;

import com.mxgraph.view.mxCellState;

import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.view.mxInteractiveCanvas;

/** 
 * @author Martin van Velsen
 */
public class INHoopEditorCanvas extends mxInteractiveCanvas
{
	protected CellRendererPane rendererPane = new CellRendererPane();
	protected JLabel vertexRenderer = new JLabel();
	protected mxGraphComponent graphComponent;

	/** 
	 * @param graphComponent
	 */
	public INHoopEditorCanvas (mxGraphComponent graphComponent)
	{
		this.graphComponent = graphComponent;

		vertexRenderer.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		vertexRenderer.setHorizontalAlignment(JLabel.CENTER);
		vertexRenderer.setBackground(graphComponent.getBackground().darker());
		vertexRenderer.setOpaque(true);
	}
	/** 
	 * @param state
	 * @param label
	 */
	public void drawVertex(mxCellState state, String label)
	{
		vertexRenderer.setText(label);
		// TODO: Configure other properties...

		rendererPane.paintComponent (g, 
									 vertexRenderer,
									 graphComponent,
									 (int) state.getX() + translate.x, 
									 (int) state.getY()
									 + translate.y, 
									 (int) state.getWidth(),
									 (int) state.getHeight(), 
									 true);
	}
}
	