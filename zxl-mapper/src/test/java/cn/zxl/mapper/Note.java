package cn.zxl.mapper;

public class Note {

	private String idNote;

	private Integer ageNote;

	private String date;

	private InnerNote innerNote;

	private String nameNote;

	public String getIdNote() {
		return idNote;
	}

	public void setIdNote(String idNote) {
		this.idNote = idNote;
	}

	public Integer getAgeNote() {
		return ageNote;
	}

	public void setAgeNote(Integer ageNote) {
		this.ageNote = ageNote;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public InnerNote getInnerNote() {
		return innerNote;
	}

	public void setInnerNote(InnerNote innerNote) {
		this.innerNote = innerNote;
	}

	public String getNameNote() {
		return nameNote;
	}

	public void setNameNote(String nameNote) {
		this.nameNote = nameNote;
	}

}
