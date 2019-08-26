package struts2;

import Core.AuthRequired;
import Core.ResultMessage;
import Persistence.Author;
import Persistence.Category;
import Persistence.Products;
import Persistence.Cnn.DAO;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ParentPackage("mainPackage")
public class ProductsManagerAction extends ActionSupport implements AuthRequired {
    private HttpServletRequest request = ServletActionContext.getRequest();
    private Map<String,Object> result = new HashMap<>();

    private String name;
    private String sinopse;
    private String value;
    private String authorId;
    private String categoryId;
    private File file;
    private String contentType;
    private String filename;

    public void setImage(File file) { this.file = file; }
    public void setImageContentType(String contentType) { this.contentType = contentType; }
    public void setImageFileName(String filename) { this.filename = filename; }

    public String getName() { return name; }
    public ProductsManagerAction setName(String name) {
        this.name = name;
        return this;
    }
    public String getSinopse() { return sinopse; }
    public ProductsManagerAction setSinopse(String sinopse) {
        this.sinopse = sinopse;
        return this;
    }
    public String getValue() { return value; }
    public ProductsManagerAction setValue(String value) {
        this.value = value;
        return this;
    }
    public String getAuthorId() { return authorId; }
    public ProductsManagerAction setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }
    public String getCategoryId() { return categoryId; }
    public ProductsManagerAction setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Map<String, Object> getResult() { return result; }
    @Override
    public String execute(){
        return SUCCESS;
    }
    @Action(value = "/system/products/add")
    public String add(){
        if(this.request.getParameterMap().size() != 0){

            Pattern patternNumber = Pattern.compile("([0-9.,]+)");
            Matcher matcherValue = patternNumber.matcher(this.value);
            if(this.name == null || this.name.equals("") || this.sinopse == null || this.sinopse.equals("") ||
                    this.value == null || this.value.equals("") || this.authorId == null || this.authorId.equals("")
                    || this.categoryId == null || this.categoryId.equals("")){
                this.result = ResultMessage.applyResult("PD00",false,"Todos os Campos para inserção do produto são obrigatórios.");
            }
            else if(!matcherValue.find()){
                this.result = ResultMessage.applyResult("PD05",false,"O valor do produto não é válido.");
            }
            else{
                Author autor= (Author) DAO.getById(Author.class,Integer.parseInt(this.authorId));
                Category category= (Category) DAO.getById(Category.class,Integer.parseInt(this.categoryId));
                if(autor == null) {
                    this.result = ResultMessage.applyResult("PD10",false,"O identificador do autor não é um valor válido, autor com esse ID não existe.");
                }
                else if(category == null) {
                    this.result = ResultMessage.applyResult("PD15",false,"O identificador da categoria selecionada não é um valor válido. Item Não existe.");
                }
                else if(Float.parseFloat(this.value.replace(",",".") ) <= 0){
                    this.result = ResultMessage.applyResult("PD20",false,"O valor do produto não pode ser zero ou negativo.");
                }
                else if(this.file != null && (!this.contentType.equals("image/jpeg") && !this.contentType.equals("image/jpg") && !this.contentType.equals("image/png") &&
                        !this.contentType.equals("image/gif"))){
                    this.result = ResultMessage.applyResult("PD25",false,"O tipo do arquivo não é válido.");
                }
                else if(this.file != null && this.file.length() > 1024 * 1024){
                    this.result = ResultMessage.applyResult("PD25",false,"O tamanho do arquivo não é válido. Max: 1Mb");
                }
                else {
                    try{
                        byte[] fileContent = FileUtils.readFileToByteArray(this.file);
                        String encodedString = Base64.getEncoder().encodeToString(fileContent);
                        Products products = new Products();
                        products.setName(this.name);
                        products.setSinopse(this.sinopse);
                        products.setValue(Float.parseFloat(this.value.replace(",",".") ));
                        products.setCategory(category);
                        products.setAuthor(autor);
                        products.setImage(encodedString);

                        boolean result = DAO.insert(products);
                        if(DAO.getError() != null && DAO.getError().getMessage().contains("Duplicate entry ")){
                            this.result = ResultMessage.applyResult("PD50",false,"Um produto com esse nome já existe no banco de dados.");
                        }
                        else if (result){
                            this.result = ResultMessage.applyResult("PD200",true,"O produto foi incluída com sucesso.");
                        }
                        else{
                            this.result = ResultMessage.applyResult("PD300", false, "O produto não pode ser salva.");
                        }
                    }
                    catch (IOException e ) {
                        System.out.println(e.getMessage());
                        this.result = ResultMessage.applyResult("PD600",false,"Ocorreu um erro no processamento do arquivo enviado. Tente novamente;");
                    }
                }
            }
        }
        else{
            this.result = ResultMessage.applyResult("AT400", false, "Nenhuma estrutura válida de dados foi recebida na requisição.");
        }
        return SUCCESS;
    }
    @Action(value = "/system/products/edit")
    public String edt(){
        String product = this.request.getParameter("id");

        if(this.request.getParameterMap().size() != 0){

            Pattern patternNumber = Pattern.compile("([0-9.]+)");
            Matcher matcherValue = patternNumber.matcher(this.value);
            if(product.equals("")){
                this.result = ResultMessage.applyResult("PD00",false,"A resolução para alteração do conteúdo não foi encontrado");
            }
            else if(this.value != null && !matcherValue.find()){
                this.result = ResultMessage.applyResult("PD05",false,"O valor do produto não é válido.");
            }
            else{

                Products objectToEdit = (Products) DAO.getById(Products.class,Integer.parseInt(product));
                if(objectToEdit == null) {
                    this.result = ResultMessage.applyResult("PD01",false,"O identificador do produto não é um valor válido, autor com esse ID não existe.");
                }
                else{
                    Author autor= this.authorId == null ? objectToEdit.getAuthor() : (Author) DAO.getById(Author.class,Integer.parseInt(this.authorId));
                    Category category= this.categoryId == null ? objectToEdit.getCategory() : (Category) DAO.getById(Category.class,Integer.parseInt(this.categoryId));

                    if(autor == null) {
                        this.result = ResultMessage.applyResult("PD10",false,"Nenhum autor com esse valor não existe.");
                    }
                    else if(category == null) {
                        this.result = ResultMessage.applyResult("PD15",false,"Nenhuma categoria encontrada com o valor fornecido.");
                    }
                    else if(this.name != null && this.name.equals("")){
                        this.result = ResultMessage.applyResult("PD30",false,"Não é possível alterar o nome do produto para um valor vazio.");
                    }
                    else if(this.value != null && this.value.equals("")){
                        this.result = ResultMessage.applyResult("PD20",false,"O valor do produto deve ser preenchido.");
                    }
                    else if(this.sinopse != null && this.sinopse.equals("")){
                        this.result = ResultMessage.applyResult("PD20",false,"Defina uma descrição válida para o produto atual.");
                    }
                    else if(this.value != null && Float.parseFloat(this.value.replace(",",".") ) <= 0){
                        this.result = ResultMessage.applyResult("PD20",false,"O valor do produto não pode ser zero ou negativo.");
                    }
                    else if(!this.contentType.equals("image/jpeg") && !this.contentType.equals("image/jpg") && !this.contentType.equals("image/png") &&
                            !this.contentType.equals("image/gif")){
                        this.result = ResultMessage.applyResult("PD25",false,"O tipo do arquivo não é válido.");
                    }
                    else if(this.file.length() > 1024 * 1024){
                        this.result = ResultMessage.applyResult("PD25",false,"O tamanho do arquivo não é válido. Max: 1Mb");
                    }
                    else {
                        try{

                            objectToEdit.setName(this.name == null ? objectToEdit.getName() : this.sinopse);
                            objectToEdit.setSinopse(this.sinopse == null ? objectToEdit.getSinopse() : this.sinopse);
                            objectToEdit.setValue(this.value == null ? objectToEdit.getValue(): Float.parseFloat(this.value) );
                            objectToEdit.setCategory(category);
                            objectToEdit.setAuthor(autor);

                            if (this.file != null) {
                                byte[] fileContent = FileUtils.readFileToByteArray(this.file);
                                String encodedString = Base64.getEncoder().encodeToString(fileContent);
                                objectToEdit.setImage(encodedString);
                            }

                            boolean result = DAO.update(objectToEdit);

                            if(DAO.getError() != null && DAO.getError().getMessage().contains("Duplicate entry ")){
                                this.result = ResultMessage.applyResult("PD50",false,"O nome do produto que voce ALTEROU coincide com outro produto já existente.");
                            }
                            else if (result){
                                this.result = ResultMessage.applyResult("PD200",true,"O produto foi atualizado com sucesso.");
                            }
                            else{
                                this.result = ResultMessage.applyResult("PD300", false, "O produto não pode ser salvo.");
                            }
                        }
                        catch (IOException e ) {
                            System.out.println(e.getMessage());
                            this.result = ResultMessage.applyResult("PD600",false,"Ocorreu um erro no processamento do arquivo enviado. Tente novamente;");
                        }
                    }
                }

            }
        }
        else{
            this.result = ResultMessage.applyResult("AT400", false, "Nenhuma estrutura válida de dados foi recebida na requisição.");
        }

        return SUCCESS;
    }

    @Action(value = "/system/products/remove")
    public String del(){
        String product = this.request.getParameter("id");

        if(product.equals("")) {
            this.result = ResultMessage.applyResult("PD10", false, "A resolução para alteração do conteúdo não foi encontrado");
        }
        else {
            Products objectToEdit = (Products) DAO.getById(Products.class,Integer.parseInt(product));
            if (objectToEdit == null){
                this.result = ResultMessage.applyResult("PD20",false,"O produto que está tentando remover não existe.");
            }
            boolean result = DAO.delete(objectToEdit);
            if (result) {
                this.result = ResultMessage.applyResult("PD200",true,"O produto foi removido com sucesso.");
            }
            else {
                this.result = ResultMessage.applyResult("PD300",false,"O produto não pôde ser removido.");
            }
        }
        return SUCCESS;
    }
}
