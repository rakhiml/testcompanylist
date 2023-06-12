package com.example.test;

import com.example.test.entity.Branch;
import com.example.test.entity.Company;
import javafx.scene.control.ListCell;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.*;

import java.util.logging.Logger;

@Getter
@Setter
public class CompanyController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;
    private ListModel<Company> companiesModel = new ListModelList<>();
    private ListModel<Branch> branchesModel = new ListModelList<>();
    private CompanyService service = new CompanyService(); //todo check injection
    @Wire
    private Listbox companies;

    @Wire
    private Listbox branches;
//    @Wire
//    private Selectbox companiesSelectbox;

    @Wire
    private Window win;
    @Wire
    private Component detailBox;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        ((ListModelList<Company>) companiesModel).clear();
        ((ListModelList<Company>) companiesModel).addAll(service.getAll());
    }

    @Listen("onSelect = #companies")
    public void showBranches(SelectEvent<Listitem, Object> event) {
        Listitem selectedItem = event.getReference();
        Listcell nameCell = (Listcell) selectedItem.getFirstChild();
        String companyID = nameCell.getLabel();
        ((ListModelList<Branch>) branchesModel).clear();
        ((ListModelList<Branch>) branchesModel).addAll(service.getBranch(Long.valueOf(companyID)));
        branches.setVisible(true);
    }

    public ListModel<Company> getCompaniesModel() {
        return companiesModel;
    }

    public void setCompaniesModel(ListModel<Company> companiesModel) {
        this.companiesModel = companiesModel;
    }
}
