package automenta.netention.swing.property;


public class NodePropertyPanel {
//    extends PropertyPanel {
//	//TODO extend OptionPropertyPanel
//	//TODO use http://google-web-toolkit.googlecode.com/svn/javadoc/1.6/com/google/gwt/user/client/ui/SuggestBox.html
//
//	final String[] nodeTypes = new String[] {
//			"is",
//			"will be",
//			"will not be"
//	};
//	private ListBox typeSelect;
//	private FlowPanel editPanel;
//	private Value value;
//	private PropertyTextBox isBox;
//	private PropertyTextBox notEqualsBox;
//	private PropertyTextBox equalsBox;
//
//	public NodePropertyPanel(String property) {
//		super(property);
//
//		typeSelect = new ListBox();
//		typeSelect.addStyleName("PropertySelect");
//		for (String s : nodeTypes) {
//			typeSelect.addItem(s);
//		}
//		typeSelect.addChangeHandler(new ChangeHandler() {
//			@Override public void onChange(ChangeEvent event) {
//				int x = typeSelect.getSelectedIndex();
//				if (x == 0)				setValue(new NodeIs());
//				else if (x == 1)		setValue(new NodeEquals());
//				else if (x == 2)		setValue(new NodeNotEquals());
//			}
//		});
//		add(typeSelect);
//
//		editPanel = new FlowPanel();
//		add(editPanel);
//
//	}
//
//	public NodePropertyPanel(String prop, NodeIs v) {
//		this(prop);
//		setValue(v);
//	}
//
//	public NodePropertyPanel(String prop, NodeEquals v) {
//		this(prop);
//		setValue(v);
//	}
//
//	public NodePropertyPanel(String prop, NodeNotEquals v) {
//		this(prop);
//		setValue(v);
//	}
//
//	protected void setValue(NodeNotEquals v) {
//		setWillBe();
//
//		this.value = v;
//
//		typeSelect.setSelectedIndex(2);
//
//		editPanel.clear();
//		notEqualsBox = new PropertyTextBox();
//		notEqualsBox.setText( v.getNode() );
//		editPanel.add(notEqualsBox);
//
//	}
//
//	protected void setValue(NodeEquals v) {
//		setWillBe();
//
//		this.value = v;
//
//		typeSelect.setSelectedIndex(1);
//
//		editPanel.clear();
//		equalsBox = new PropertyTextBox();
//		equalsBox.setText( v.getNode() );
//		editPanel.add(equalsBox);
//
//	}
//
//	protected void setValue(NodeIs v) {
//		setIs();
//
//		this.value = v;
//
//		typeSelect.setSelectedIndex(0);
//
//		editPanel.clear();
//		isBox = new PropertyTextBox();
//		isBox.setText( v.getNode() );
//		editPanel.add(isBox);
//
//	}
//
//	@Override public void widgetToValue() {
//	}
//
//
}
