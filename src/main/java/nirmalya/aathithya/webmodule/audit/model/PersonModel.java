/**
 * 
 */
package nirmalya.aathithya.webmodule.audit.model;

/**
 * @author USER
 *
 */
public class PersonModel {
	private String value;
	private String label;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public PersonModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
