package org.pos.coffee.bean;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.evey.annotation.UniqueField;
import org.evey.bean.BaseEntity;
import org.evey.bean.ReferenceLookUp;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="ITEM")
public class Item extends BaseEntity {

	private static final long serialVersionUID = 2776590636632885067L;

	@Column(name="ITEM_CODE", unique = true, nullable = false)
	@UniqueField
	private String itemCode;

	@Column(name="ITEM_NAME")
	private String itemName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="UOM")
	private ReferenceLookUp uom;

	@Column(name = "UOM", insertable = false, updatable = false)
	private Long uomId;

	@Column(name="CRITICAL_LEVEL")
	private Double criticalLevel;

	@Column(name="DEMARCATION")
	private Double demarcation;

	@Column(name = "IS_BASE")
	private Boolean isBase;

	@Transient
	private Boolean isSelectedAsBase;

	@Column(name = "UNIT_PRICE")
	private Double unitPrice;

	@OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<Stock> stockList;
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ReferenceLookUp getUom() {
		return uom;
	}
	public void setUom(ReferenceLookUp uom) {
		this.uom = uom;
	}
	public Long getUomId() {
		return uomId;
	}
	public void setUomId(Long uomId) {
		this.uomId = uomId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getCriticalLevel() {
		return criticalLevel;
	}

	public void setCriticalLevel(Double criticalLevel) {
		this.criticalLevel = criticalLevel;
	}

	public Double getDemarcation() {
		return demarcation;
	}

	public void setDemarcation(Double demarcation) {
		this.demarcation = demarcation;
	}

	public Boolean getIsBase() {
		return isBase;
	}

	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}

	public Boolean getIsSelectedAsBase() {
		return isSelectedAsBase;
	}

	public void setIsSelectedAsBase(Boolean isSelectedAsBase) {
		this.isSelectedAsBase = isSelectedAsBase;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
	protected void prePersist() {
		if(this.criticalLevel==null){
			this.criticalLevel = 0D;
		}
		if(this.demarcation==null){
			this.demarcation = 0D;
		}

		if(this.isBase==null){
			this.isBase = false;
		}
	}
}
