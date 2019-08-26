package Core;

/**
 * Interface Curinga utilizada para
 * identificar ACTIONS STRUTS que precisam de
 * autenticação de usuário para acessar dados
 * restritos, nenhuma assinatura é necessária
 * para essa interface em questão.
 * Contudo, quando o interceptador global GUARD
 * é executado em todas as requisições e identificar
 * um classe ACTION que requer autenticação, ou seja
 * a classe implementa essa interface curinga, a validação
 * de sessaão é disparada.
 * */
public interface AuthRequired { }
