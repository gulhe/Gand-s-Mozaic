package org.gulhe.scatter.properties;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class SelfAwareProperty extends Properties {

	private String name;

	public String getName() {
		return name;
	}

	public SelfAwareProperty(String propFileName) {
		super();
		this.name = propFileName;
	}
	
	public void save() throws IOException{
		OutputStream oPS = new FileOutputStream(name);
		this.store(oPS, "Comments ?");
	};

	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SelfAwareProperty other = (SelfAwareProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
