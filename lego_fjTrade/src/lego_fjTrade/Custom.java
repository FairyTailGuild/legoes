package lego_fjTrade;

import java.util.List;
import java.util.HashMap;
import framework.sdk.DbHandler;
import framework.sdk.Framework;
import framework.sdk.CustomAction;
import library.database.DatabaseKit;
import library.string.CharacterString;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

public class Custom extends CustomAction {
        // private HttpServlet httpServlet;
        // private HttpServletRequest httpServletRequest;
        // private HttpServletResponse httpServletResponse;
        private HashMap<String, Object> parameter;
        // private DbHandler dbHandler;
        private SqlSession sqlSession;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                // this.httpServlet = httpServlet;
                // this.httpServletRequest = httpServletRequest;
                // this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                // this.dbHandler = dbHandler;
        }

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, SqlSession sqlSession, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, sqlSession, parameter);
                // this.httpServletRequest = httpServletRequest;
                // this.httpServletResponse = httpServletResponse;
                this.parameter = parameter;
                this.sqlSession = sqlSession;
        }

        /**
         * （基于类内）修改合同编号
         * 
         * @param s 执行SqlSession
         * @param contract_uuid 合同的uuid
         * @param contractSelectNamespace 查询合同所在数据的名空间（如：物流合同中的lego_fjTrade.selectContractLogistics）
         * @param contractUpdateNamespace 更新文件集群所在数据的名空间（如：物流合同中的lego_fjTrade.updateContractLogistics）
         * @param buyerColumnName 合同买方uuid的列名（如：物流合同中的employer_uuid）
         * @param sellerColumnName 合同卖方uuid的列名（如：物流合同中的logistics_uuid）
         * 
         * @return 1: 更新合同编号成功<br />
         *         -1: 没有找到contract_uuid对应的物流合同<br />
         *         -2: 没有找到买方企业的uuid<br />
         *         -3: 没有找到卖方企业的uuid<br />
         *         -4: 更新合同编号失败<br />
         */
        public int inline_modifyContractCode(SqlSession s, String contract_uuid, String contractSelectNamespace, String contractUpdateNamespace, String buyerColumnName, String sellerColumnName) {
                try {
                        // [内部变量声明]
                        HashMap<String, Object> p = null;
                        HashMap<String, Object> hm = null;
                        List<HashMap<String, Object>> list = null;
                        int res = 0;
                        // [开始逻辑判断]
                        // 检查contract_uuid对应的物流合同是否存在
                        p = new HashMap<String, Object>();
                        p.put("uuid", contract_uuid);
                        list = s.selectList(contractSelectNamespace, p);
                        if (!DatabaseKit.hasData(list)) {
                                // 没有找到contract_uuid对应的物流合同
                                return -1;
                        }
                        hm = list.iterator().next();
                        String buyerUuid = (String) hm.get(buyerColumnName);
                        String sellerUuid = (String) hm.get(sellerColumnName);
                        // 检查买方企业是否存在
                        p = new HashMap<String, Object>();
                        p.put("uuid", buyerUuid);
                        list = s.selectList("lego_crm.selectEnterpriseInformation", p);
                        if (!DatabaseKit.hasData(list)) {
                                // 没有找到买方企业的uuid
                                return -2;
                        }
                        hm = list.iterator().next();
                        String buyerShortName = (String) hm.get("short_name");
                        String buyerPinYinShort = CharacterString.getPinYinShort(buyerShortName, true);
                        // 检查卖方企业是否存在
                        p = new HashMap<String, Object>();
                        p.put("uuid", sellerUuid);
                        list = s.selectList("lego_crm.selectEnterpriseInformation", p);
                        if (!DatabaseKit.hasData(list)) {
                                // 没有找到卖方企业的uuid
                                return -3;
                        }
                        hm = list.iterator().next();
                        String sellerShortName = (String) hm.get("short_name");
                        String sellerPinYinShort = CharacterString.getPinYinShort(sellerShortName, true);
                        String contract_code = null;
                        for (;;) {
                                contract_code = buyerPinYinShort + "-" + sellerPinYinShort + "-" + CharacterString.getCurrentFormatDateTime("yyMdSSS");
                                // 检查合同编号是否存在
                                p = new HashMap<String, Object>();
                                p.put("contract_code", contract_code);
                                list = s.selectList(contractSelectNamespace, p);
                                if (!DatabaseKit.hasData(list)) {
                                        // 如果合同编号是唯一的，那么跳出循环。
                                        break;
                                }
                        }
                        // 更新合同编号
                        p = new HashMap<String, Object>();
                        p.put("uuid", contract_uuid);
                        p.put("contract_code", contract_code);
                        res = s.update(contractUpdateNamespace, p);
                        if (1 > res) {
                                // 更新合同编号失败
                                return -4;
                        }
                        // 更新合同编号成功
                        return 1;
                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }

        /**
         * （基于事务）修改物流合同编号
         * 
         * 参数列表所需参数：<br />
         * contract_uuid: 物流合同的uuid<br />
         * contractSelectNamespace: 查询合同所在数据的名空间（如：物流合同中的lego_fjTrade.selectContractLogistics）<br />
         * contractUpdateNamespace: 更新文件集群所在数据的名空间（如：物流合同中的lego_fjTrade.updateContractLogistics）<br />
         * buyerColumnName: 合同买方uuid的列名（如：物流合同中的employer_uuid）<br />
         * sellerColumnName: 合同卖方uuid的列名（如：物流合同中的logistics_uuid）<br />
         * 
         * @return 1: 成功<br />
         *         -1: 失败<br />
         *         0: 异常<br />
         */
        public int t_modifyContractCode() {
                try {
                        // [接收所需参数]
                        String contract_uuid = (String) this.parameter.get("contract_uuid");
                        String contractSelectNamespace = (String) this.parameter.get("contractSelectNamespace");
                        String contractUpdateNamespace = (String) this.parameter.get("contractUpdateNamespace");
                        String buyerColumnName = (String) this.parameter.get("buyerColumnName");
                        String sellerColumnName = (String) this.parameter.get("sellerColumnName");
                        // [开始逻辑判断]
                        if (1 == this.inline_modifyContractCode(this.sqlSession, contract_uuid, contractSelectNamespace, contractUpdateNamespace, buyerColumnName, sellerColumnName)) {
                                return 1;
                        }
                        return -1;

                } catch (Exception e) {
                        Framework.LOG.warn(e.toString());
                        return 0;
                }
        }
}
