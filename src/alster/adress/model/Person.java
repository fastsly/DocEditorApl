package alster.adress.model;

import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class Person {

	private final StringProperty firstName;
    private final StringProperty lastName;
    private final ObjectProperty<LocalDate> street;
    private final ObjectProperty<LocalDate> postalCode;
    private final ObjectProperty<LocalDate> city;
    private final ObjectProperty<LocalDate> birthday;
    private final ObjectProperty<LocalDate> dataNastere;
    private final IntegerProperty consiNr;
    private StringProperty evalFileName;
    private StringProperty formatAdaptatFileName;
    private StringProperty planDeInterventieFileName;
    private StringProperty reabSocFileName;
    private StringProperty reabFunctFileName;
    private BooleanProperty absent;


    public Person() {
        this(null, null);
    }

    public Person(String firstName, String lastName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);

        // Some initial dummy data, just for convenient testing.
        this.street = new SimpleObjectProperty<LocalDate>(LocalDate.of(2018, 01, 1));
        this.postalCode = new SimpleObjectProperty<LocalDate>(LocalDate.of(2018, 01, 1));
        this.city = new SimpleObjectProperty<LocalDate>(LocalDate.of(2018, 01, 1));
        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(2018, 01, 1));
        this.dataNastere = new SimpleObjectProperty<LocalDate>(LocalDate.of(1950, 01, 1));
        this.consiNr = new SimpleIntegerProperty (8);
        this.absent = new SimpleBooleanProperty(true);
        this.evalFileName = new SimpleStringProperty("fileName");
        this.formatAdaptatFileName = new SimpleStringProperty("fileName");
        this.planDeInterventieFileName = new SimpleStringProperty("fileName");
        this.reabSocFileName = new SimpleStringProperty("fileName");
        this.reabFunctFileName = new SimpleStringProperty("fileName");
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    @XmlJavaTypeAdapter (alster.adress.util.LocalDateAdapter.class)
    public LocalDate getStreet() {
        return street.get();
    }

    public void setStreet(LocalDate street) {
        this.street.set(street);
    }

    public ObjectProperty<LocalDate> streetProperty() {
        return street;
    }

    @XmlJavaTypeAdapter (alster.adress.util.LocalDateAdapter.class)
    public LocalDate getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(LocalDate postalCode) {
        this.postalCode.set(postalCode);
    }

    public ObjectProperty<LocalDate> postalCodeProperty() {
        return postalCode;
    }

    @XmlJavaTypeAdapter (alster.adress.util.LocalDateAdapter.class)
    public LocalDate getCity() {
        return city.get();
    }

    public void setCity(LocalDate city) {
        this.city.set(city);
    }

    public ObjectProperty<LocalDate> cityProperty() {
        return city;
    }

    @XmlJavaTypeAdapter (alster.adress.util.LocalDateAdapter.class)
    public LocalDate getBirthday() {
        return birthday.get();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday.set(birthday);
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        return birthday;
    }
    @XmlJavaTypeAdapter (alster.adress.util.LocalDateAdapter.class)
    public LocalDate getDataNastere (){
    	return dataNastere.get();
    }
    
    public void setDataNastere(LocalDate nastere){
    	this.dataNastere.set(nastere);
    }
    public ObjectProperty<LocalDate> nastereProperty(){
    	return dataNastere;
    }

    public void setMonitFileName( LocalDate s){
    	this.dataNastere.set(s);
    }

    public ObjectProperty<LocalDate> dataNastereProperty() {
        return dataNastere;
    }

    public String getEvalFileName (){
    	return evalFileName.get();
    }

    public void setEvalFileName( String s){
    	this.evalFileName.set(s);
    }

    public String getFormatAdaptatFileName (){
    	return formatAdaptatFileName.get();
    }

    public void setFormatAdaptatFileName( String s){
    	this.formatAdaptatFileName.set(s);
    }

    public String getPlanDeInterventieFileName (){
    	return planDeInterventieFileName.get();
    }

    public void setPlanDeInterventieFileName( String s){
    	this.planDeInterventieFileName.set(s);
    }

    public String getReabSocFileName (){
    	return reabSocFileName.get();
    }

    public void setReabSocFileName( String s){
    	this.reabSocFileName.set(s);
    }

    public String getReabFunctFileName (){
    	return reabFunctFileName.get();
    }

    public void setReabFunctFileName( String s){
    	this.reabFunctFileName.set(s);
    }

    public boolean getAbsent(){
    	return absent.get();
    }

    public void setAbsent( boolean b){
    	this.absent.set(b);
    }

    public BooleanProperty absentProperty(){
    	return absent;
    }

    public int getConsiNr(){
    	return consiNr.get();
    }

    public void setConsiNr (int nr){
    	this.consiNr.set(nr);
    }

    public IntegerProperty consiNrProperty() {
        return consiNr;
    }
}
