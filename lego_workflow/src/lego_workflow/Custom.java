package lego_workflow;

//import java.util.List;
import java.util.HashMap;
//import java.util.Iterator;
//import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import framework.sdk.Message;
import framework.sdk.DbHandler;
//import framework.sdk.Framework;
import framework.sdk.CustomAction;
//import library.database.DatabaseKit;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.apache.ibatis.session.SqlSession;

public class Custom extends CustomAction {
        // private HttpServlet httpServlet;
        // private HttpServletRequest httpServletRequest;
        // private HttpServletResponse httpServletResponse;
        // private HashMap<String, Object> parameter;
        // private DbHandler dbHandler;
        // 通讯消息对象
        // private lego_communication.Custom communication;
        // private SqlSession sqlSession;

        public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, DbHandler dbHandler, HashMap<String, Object> parameter) {
                super(httpServlet, httpServletRequest, httpServletResponse, dbHandler, parameter);
                // this.httpServlet = httpServlet;
                // this.httpServletRequest = httpServletRequest;
                // this.httpServletResponse = httpServletResponse;
                // this.parameter = parameter;
                // this.dbHandler = dbHandler;
                // this.communication = new lego_communication.Custom(this.httpServlet, this.httpServletRequest, this.httpServletResponse, this.dbHandler, this.parameter);
        }

        // public Custom(HttpServlet httpServlet, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, SqlSession sqlSession, HashMap<String, Object> parameter) {
        // super(httpServlet, httpServletRequest, httpServletResponse, sqlSession, parameter);
        // this.httpServletRequest = httpServletRequest;
        // this.httpServletResponse = httpServletResponse;
        // this.parameter = parameter;
        // this.communication = new lego_communication.Custom(this.httpServlet, this.httpServletRequest, this.httpServletResponse, this.dbHandler, this.parameter);
        // this.sqlSession = sqlSession;
        // }

        /**
         * 迭代遍历获取部门的所有父部门
         * 
         * @param s 执行SqlSession
         * @param list 返回列表
         * @param uuid 部门uuid
         * @return 1 操作成功<br />
         *         -1 没有找到部门的数据<br />
         *         -2 迭代遍历出现错误<br />
         *         0 未知错误<br />
         */
        // public int inline_getParentDepartment(SqlSession s, ArrayList<String> list, String uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> l = null;
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // l = s.selectList("lego_workflow.selectDepartment", p);
        // if (!DatabaseKit.hasData(l)) {
        // return -1;
        // }
        // hm = l.iterator().next();
        // String parent_uuid = (String) hm.get("parent_uuid");
        // list.add(parent_uuid);
        // if (-2 == this.inline_getParentDepartment(s, list, parent_uuid)) {
        // return -2;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 迭代遍历获取部门下包括的所有子部门
         * 
         * @param s 执行SqlSession
         * @param list 返回列表
         * @param parent_uuid 父部门uuid
         * @return 1 操作成功<br />
         *         -1 没有找到部门的数据<br />
         *         -2 迭代遍历出现错误<br />
         *         0 未知错误<br />
         */
        // public int inline_getChildDepartment(SqlSession s, ArrayList<String> list, String parent_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> l = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // p = new HashMap<String, Object>();
        // p.put("parent_uuid", parent_uuid);
        // l = s.selectList("lego_workflow.selectDepartment", p);
        // if (!DatabaseKit.hasData(l)) {
        // return -1;
        // }
        // iter = l.iterator();
        // while (iter.hasNext()) {
        // hm = iter.next();
        // String uuid = (String) hm.get("uuid");
        // list.add(uuid);
        // if (-2 == this.inline_getChildDepartment(s, list, uuid)) {
        // return -2;
        // }
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 获取员工岗位信息
         * 
         * @param s 执行SqlSession
         * @param array 返回列表
         * @param employeeUuid 员工uuid
         * @return 1 操作成功<br />
         *         -1 没有找到岗位的数据<br />
         *         0 未知错误<br />
         */
        // public int inline_getEmployeePosition(SqlSession s, JSONArray array, String employeeUuid) {
        // try {
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> l = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // l = s.selectList("lego_workflow.selectPosition");
        // if (!DatabaseKit.hasData(l)) {
        // return -1;
        // }
        // iter = l.iterator();
        // while (iter.hasNext()) {
        // hm = iter.next();
        // String employee_list = (String) hm.get("employee_list");
        // String employeeArr[] = employee_list.split(";");
        // for (int i = 0; i < employeeArr.length; i++) {
        // String uuid = employeeArr[i];
        // if (uuid.equalsIgnoreCase(employeeUuid)) {
        // JSONObject obj = new JSONObject();
        // obj.put("uuid", hm.get("uuid"));
        // obj.put("department_uuid", hm.get("department_uuid"));
        // obj.put("name", hm.get("name"));
        // obj.put("description", hm.get("description"));
        // array.put(obj);
        // break;
        // }
        // }
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）删除记录
         * 
         * @param s 执行SqlSession
         * @param uuid 记录uuid
         * 
         * @return 1: 删除成功<br />
         *         -1: 删除失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_removeRecord(SqlSession s, String uuid) {
        // try {
        // HashMap<String, Object> p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // int res = s.delete("lego_workflow.deleteRecord", p);
        // if (1 > res) {
        // return -1;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）删除作业
         * 
         * @param s 执行SqlSession
         * @param uuid 作业uuid
         * 
         * @return 1: 删除成功<br />
         *         -1: 删除记录失败<br />
         *         -2: 删除作业附件失败<br />
         *         -3: 删除作业失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_removeTask(SqlSession s, String uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> list = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // p = new HashMap<String, Object>();
        // p.put("task_uuid", uuid);
        // list = s.selectList("lego_workflow.selectRecord", p);
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // hm = (HashMap<String, Object>) iter.next();
        // String recordUuid = (String) hm.get("uuid");
        // if (1 != this.inline_removeRecord(s, recordUuid)) {
        // return -1;
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // list = s.selectList("lego_workflow.selectTask", p);
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // hm = (HashMap<String, Object>) iter.next();
        // String cluster_list = (String) hm.get("cluster_list");
        // if (null != cluster_list) {
        // String cluster_name[] = cluster_list.split(";");
        // for (int i = 0; i < cluster_name.length; i++) {
        // lego_storage.Custom c = new lego_storage.Custom(this.httpServlet, this.httpServletRequest, this.httpServletResponse, this.dbHandler, this.parameter);
        // if (1 != c.inline_removeFile(s, cluster_name[i], null)) {
        // return -2;
        // }
        // }
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // int res = s.delete("lego_workflow.deleteTask", p);
        // if (1 > res) {
        // return -3;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）删除流程
         * 
         * @param s 执行SqlSession
         * @param uuid 流程uuid
         * 
         * @return 1: 删除成功<br />
         *         -1: 删除作业失败<br />
         *         -2: 删除流程失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_removeProcess(SqlSession s, String uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> list = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // p = new HashMap<String, Object>();
        // p.put("process_uuid", uuid);
        // list = s.selectList("lego_workflow.selectTask", p);
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // hm = (HashMap<String, Object>) iter.next();
        // String taskUuid = (String) hm.get("uuid");
        // if (1 != this.inline_removeTask(s, taskUuid)) {
        // return -1;
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // int res = s.delete("lego_workflow.deleteProcess", p);
        // if (1 > res) {
        // return -2;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）删除岗位
         * 
         * @param s 执行SqlSession
         * @param uuid 岗位编号
         * 
         * @return 1: 删除成功<br />
         *         -1: 更新工作流审批岗位列表失败<br />
         *         -2: 删除工作流审批岗位失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_removePosition(SqlSession s, String uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // int res = 0;
        // list = s.selectList("lego_workflow.selectProcess");
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // boolean hasData = false;
        // String newList = "";
        // HashMap<String, Object> hm = (HashMap<String, Object>) iter.next();
        // String position_list = (String) hm.get("position_list");
        // String positionArr[] = position_list.split(";");
        // // 遍历流程中所有的有影响的列表
        // for (int i = 0; i < positionArr.length; i++) {
        // if (positionArr[i].equalsIgnoreCase((String) uuid)) {
        // // 逐一判断列表中是否包括当前编号
        // hasData = true;
        // } else {
        // newList += positionArr[i];
        // newList += ";";
        // }
        // }
        // if (hasData) {
        // p = new HashMap<String, Object>();
        // p.put("position_list", newList);
        // // 如果包括则更新流程的审批岗位列表
        // res = s.update("lego_workflow.updateProcess", p);
        // if (1 > res) {
        // return -1;
        // }
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // res = s.delete("lego_workflow.deletePosition", p);
        // if (1 > res) {
        // return -2;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）删除部门
         * 
         * @param s 执行SqlSession
         * @param uuid 部门uuid
         * 
         * @return 1: 删除成功<br />
         *         -1: 获取子部门失败<br />
         *         -2: 删除部门下流程失败<br />
         *         -3: 删除部门下岗位失败<br />
         *         -4: 删除部门失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_removeDepartment(SqlSession s, String uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> list = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // ArrayList<String> depList = null;
        // Iterator<String> depIter = null;
        // int res = 0;
        // // 获取部门下的包括当前部门在内，以及所有子部门。
        // depList = new ArrayList<String>();
        // depList.add(uuid);
        // if (-2 == this.inline_getChildDepartment(s, depList, uuid)) {
        // return -1;
        // }
        // depIter = depList.iterator();
        // while (depIter.hasNext()) {
        // String depUuid = depIter.next();
        // // 获取部门下所有工作流程
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", depUuid);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // hm = (HashMap<String, Object>) iter.next();
        // // String processUuid = (String) hm.get("uuid");
        // // if (1 != this.inline_removeProcess(s, processUuid)) {
        // // return -2;
        // // }
        // }
        // // 获取部门下所有的岗位
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", depUuid);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // hm = (HashMap<String, Object>) iter.next();
        // String positionUuid = (String) hm.get("uuid");
        // if (1 != this.inline_removePosition(s, positionUuid)) {
        // return -3;
        // }
        // }
        // // 删除部门数据
        // p = new HashMap<String, Object>();
        // p.put("uuid", depUuid);
        // res = s.delete("lego_workflow.deleteDepartment", p);
        // if (1 > res) {
        // return -4;
        // }
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）删除员工
         * 
         * @param s 执行SqlSession
         * @param user_uuid 用户uuid
         * 
         * @return 1: 删除成功<br />
         *         -1: 删除员工失败<br />
         *         -1: 删除用户失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_removeEmployee(SqlSession s, String user_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // // HashMap<String, Object> hm = null;
        // // List<HashMap<String, Object>> list = null;
        // // Iterator<HashMap<String, Object>> iter = null;
        // int res = 0;
        // p = new HashMap<String, Object>();
        // p.put("user_uuid", user_uuid);
        // res = s.delete("lego_workflow.deleteEmployee", p);
        // if (1 > res) {
        // return -1;
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", user_uuid);
        // res = s.delete("lego_user.deleteUser", p);
        // if (1 > res) {
        // return -2;
        // }
        // // // 获取所有工作流程
        // // list = s.selectList("lego_workflow.selectProcess");
        // // iter = list.iterator();
        // // while (iter.hasNext()) {
        // // boolean hasData = false;
        // // String newList = "";
        // // hm = (HashMap<String, Object>) iter.next();
        // // String cc_list = (String) hm.get("cc_list");
        // // String ccArr[] = cc_list.split(";");
        // // // 遍历流程中所有的有影响的列表
        // // for (int i = 0; i < ccArr.length; i++) {
        // // if (ccArr[i].equalsIgnoreCase(uuid)) {
        // // // 逐一判断列表中是否包括当前编号
        // // hasData = true;
        // // } else {
        // // newList += ccArr[i];
        // // newList += ";";
        // // }
        // // }
        // // if (hasData) {
        // // p = new HashMap<String, Object>();
        // // p.put("cc_list", newList);
        // // // 如果包括则更新流程的审批岗位列表
        // // res = s.update("lego_workflow.updateProcess", p);
        // // if (1 > res) {
        // // return -1;
        // // }
        // // }
        // // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）添加岗位
         * 
         * @param s 执行SqlSession
         * @param department_uuid 部门uuid
         * @param name 岗位名称
         * @param description 岗位描述
         * @param employee_list 岗位包含的员工列表
         * @param order_offset 岗位排序偏移
         * 
         * @return 1: 添加成功<br />
         *         -1: 没有找到与department_uuid对应的部门数据<br />
         *         -2: 部门下岗位名称已有重名<br />
         *         -3: 没有找到岗位里的员工信息<br />
         *         -4: 添加岗位失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_addPosition(SqlSession s, String department_uuid, String name, String description, String employee_list, Integer order_offset) {
        // try {
        // // [内部变量声明]
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // int res = 0;
        // // [开始逻辑判断]
        // // 检查部门编号是否已存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", department_uuid);
        // list = s.selectList("lego_workflow.selectDepartment", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 没有找到与department_uuid对应的部门数据
        // return -1;
        // }
        // // 检查同一部门下名称是否已存在
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", department_uuid);
        // p.put("name", name);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // if (DatabaseKit.hasData(list)) {
        // // 部门下岗位名称已有重名
        // return -2;
        // }
        // // 检查employee_list中的每一个用户编号是否存在
        // if (null != employee_list) {
        // String arr[] = employee_list.split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // if (!DatabaseKit.hasData(list)) {
        // // 没有找到岗位里的员工信息
        // return -3;
        // }
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", department_uuid);
        // p.put("name", name);
        // p.put("description", description);
        // p.put("employee_list", employee_list);
        // p.put("order_offset", order_offset);
        // res = s.insert("lego_workflow.insertPosition", p);
        // if (1 > res) {
        // // 添加岗位失败
        // return -4;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 修改工作流流程岗位信息
         * 
         * @param s 执行SqlSession
         * @param uuid 岗位uuid
         * @param department_uuid 部门uuid
         * @param name 岗位名称
         * @param description 岗位描述
         * @param employee_list 岗位员工列表
         * @param order_offset 排序偏移
         * 
         * @return 1: 修改成功<br />
         *         -1: 没有找到部门<br />
         *         -2: 部门下岗位名称已有重名<br />
         *         -3: 没有找到岗位设置的员工信息<br />
         *         -4: 修改工作流岗位出错<br />
         *         0: 未知错误<br />
         */
        // public int inline_modifyPosition(SqlSession s, String uuid, String department_uuid, String name, String description, String employee_list, Object order_offset) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // // 检查部门编号是否已存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", department_uuid);
        // list = s.selectList("lego_workflow.selectDepartment", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -1;
        // }
        // // 检查同一岗位下名称是否已存在
        // p = new HashMap<String, Object>();
        // p.put("ne_uuid", uuid);
        // p.put("department_uuid", department_uuid);
        // p.put("name", name);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // if (DatabaseKit.hasData(list)) {
        // return -2;
        // }
        // // 检查employee_list中的每一个用户编号是否存在
        // if (null != employee_list) {
        // String arr[] = employee_list.split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -3;
        // }
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // p.put("department_uuid", department_uuid);
        // p.put("name", name);
        // p.put("description", description);
        // p.put("employee_list", employee_list);
        // p.put("order_offset", order_offset);
        // int res = s.update("lego_workflow.updatePosition", p);
        // if (1 > res) {
        // return -4;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 添加工作流流程
         * 
         * @param s 执行SqlSession
         * @param department_uuid 部门uuid
         * @param icon 工作流图标
         * @param name 工作流名称
         * @param description 工作流描述
         * @param content 工作流内容
         * @param position_list 工作流审批岗位列表
         * @param cc_list 工作流抄送岗位列表
         * @param status 工作流状态
         * 
         * @return 1 添加成功<br />
         *         -1 没有找到对应部门<br />
         *         -2 部门下工作流已有重名<br />
         *         -3 工作流审批岗位信息没有找到<br />
         *         -4 工作流员工抄送信息没有找到<br />
         *         -5 添加工作流程出错<br />
         *         0 未知错误<br />
         */
        // public int inline_addProcess(SqlSession s, String department_uuid, Object icon, Object name, Object description, Object content, String position_list, String cc_list, Object status) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // int res = 0;
        // // 检查部门编号是否已存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", department_uuid);
        // list = s.selectList("lego_workflow.selectDepartment", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -1;
        // }
        // // 检查同一流程下名称是否已存在
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", department_uuid);
        // p.put("name", name);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // if (DatabaseKit.hasData(list)) {
        // return -2;
        // }
        // // 检查position_list中的每一个岗位编号是否存在
        // if (null != position_list) {
        // String arr[] = position_list.split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -3;
        // }
        // }
        // }
        // // 检查cc_list中的每一个员工编号是否存在
        // if (null != cc_list) {
        // String arr[] = cc_list.split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -4;
        // }
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", department_uuid);
        // p.put("icon", icon);
        // p.put("name", name);
        // p.put("description", description);
        // p.put("content", content);
        // p.put("position_list", position_list);
        // p.put("cc_list", cc_list);
        // p.put("status", status);
        // res = s.insert("lego_workflow.insertProcess", p);
        // if (1 > res) {
        // return -5;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 修改工作流流程
         * 
         * @param s 执行SqlSession
         * @param uuid 工作流uuid
         * @param department_uuid 部门uuid
         * @param icon 工作流图标
         * @param name 工作流名称
         * @param description 工作流描述
         * @param content 工作流内容
         * @param position_list 工作流审批岗位列表
         * @param cc_list 工作流抄送岗位列表
         * @param status 工作流状态
         * 
         * @return 1 修改成功<br />
         *         -1 没有找到对应部门<br />
         *         -2 部门下工作流已有重名<br />
         *         -3 工作流审批岗位信息没有找到<br />
         *         -4 工作流员工抄送信息没有找到<br />
         *         -5 修改工作流程出错<br />
         *         0 未知错误<br />
         */
        // public int inline_modifyProcess(SqlSession s, String uuid, String department_uuid, Object icon, Object name, Object description, Object content, String position_list, String cc_list, Object status) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // int res = 0;
        // // 检查部门编号是否已存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", department_uuid);
        // list = s.selectList("lego_workflow.selectDepartment", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -1;
        // }
        // // 检查同一流程下名称是否已存在
        // p = new HashMap<String, Object>();
        // p.put("ne_uuid", uuid);
        // p.put("department_uuid", department_uuid);
        // p.put("name", name);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // if (DatabaseKit.hasData(list)) {
        // return -2;
        // }
        // // 检查position_list中的每一个岗位编号是否存在
        // if (null != position_list) {
        // String arr[] = position_list.split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -3;
        // }
        // }
        // }
        // // 检查cc_list中的每一个员工编号是否存在
        // if (null != cc_list) {
        // String arr[] = cc_list.split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -4;
        // }
        // }
        // }
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // p.put("department_uuid", department_uuid);
        // p.put("icon", icon);
        // p.put("name", name);
        // p.put("description", description);
        // p.put("content", content);
        // p.put("position_list", position_list);
        // p.put("cc_list", cc_list);
        // p.put("status", status);
        // res = s.update("lego_workflow.updateProcess", p);
        // if (1 > res) {
        // return -5;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 添加工作流作业
         * 
         * @param s 执行SqlSession
         * @param uuid uuid
         * @param process_id 工作流uuid
         * @param content 作业内容
         * @param cluster_list 文件集群
         * @param cc_list 审批通过后抄送的员工（分号分隔的员工编号）
         * @param creator_uuid 创建者uuid
         * @param status 审批状态
         * @return 1 添加成功<br />
         *         -1 没有找到工作流数据<br />
         *         -2 没有找到工作流附加的文件集群<br />
         *         -3 保存附件为正式文件出错<br />
         *         -4 工作流员工抄送信息没有找到<br />
         *         -5 新增工作流作业出错<br />
         *         0 未知错误<br />
         */
        // public int inline_addTask(SqlSession s, String uuid, String process_uuid, Object content, Object cluster_list, Object cc_list, Object creator_uuid, Object status) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // // 检查process_uuid是否已存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", process_uuid);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -1;
        // }
        // HashMap<String, Object> processHm = list.iterator().next();
        // String position_list_arr[] = ((String) processHm.get("position_list")).split(";");
        // // 如果包含附件，检查是否合法
        // // if (null != cluster_list) {
        // // String cluster_name[] = ((String) cluster_list).split(";");
        // // for (int i = 0; i < cluster_name.length; i++) {
        // // lego_storage.Custom c = new lego_storage.Custom(this.httpServlet, this.httpServletRequest, this.httpServletResponse, this.dbHandler, this.parameter);
        // // if (1 != c.inline_checkFileExist(s, cluster_name[i], "-1", null, creator_uuid)) {
        // // return -2;
        // // }
        // // if (1 != c.inline_saveFile(s, cluster_name[i], creator_uuid)) {
        // // return -3;
        // // }
        // // }
        // // }
        // // 检查cc_list中的每一个员工编号是否存在
        // if (null != cc_list) {
        // String arr[] = ((String) cc_list).split(";");
        // for (int i = 0; i < arr.length; i++) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", arr[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -4;
        // }
        // }
        // }
        // // 获取下个岗位下的员工编号集合
        // p = new HashMap<String, Object>();
        // p.put("uuid", position_list_arr[0]);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // Iterator<HashMap<String, Object>> iter = list.iterator();
        // String nextUsers = "";
        // while (iter.hasNext()) {
        // HashMap<String, Object> positionHm = iter.next();
        // String employee_list[] = ((String) positionHm.get("employee_list")).split(";");
        // for (int i = 0; i < employee_list.length; i++) {
        // // 员工编号转为用户编号
        // p = new HashMap<String, Object>();
        // p.put("uuid", employee_list[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // Iterator<HashMap<String, Object>> employeeIter = list.iterator();
        // while (employeeIter.hasNext()) {
        // HashMap<String, Object> employeeHm = employeeIter.next();
        // nextUsers += (String) employeeHm.get("user_uuid") + ";";
        // }
        // }
        // }
        // if (1 != this.communication.inline_addMessage(s, null/* 没有附件时，directory_uuid为空 */, null/* 没有附件时，file_status为空 */, 3/* 消息类型（工作流审批消息） */, uuid/* 标题（uuid） */, 2/* 内容（审批结果：等待审批） */, 4/* 内容类型（通知消息） */, 0/* 发件人编号 */, nextUsers/* 收件人编号集合 */, null/* 抄送人编号集合 */, null/* 密送人编号集合 */, null/* 附件集群集合 */, 0/* 读取状态 */)) {
        // Framework.LOG.error("Send Workflow Notify Message Error");
        // }
        // // 新增作业
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // p.put("process_uuid", process_uuid);
        // p.put("content", content);
        // p.put("cluster_list", cluster_list);
        // p.put("position_offset", 0);
        // p.put("cc_list", cc_list);
        // p.put("creator_uuid", creator_uuid);
        // p.put("status", status);
        // int res = s.insert("lego_workflow.insertTask", p);
        // if (1 > res) {
        // return -5;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 添加用户和员工
         * 
         * @param s 执行SqlSession
         * @param userUuid 用户的uuid
         * @param userName 用户名
         * @param password 密码
         * @param wechatOpenid 微信的openid
         * @param role 角色
         * @param failedRetryCount 失败重试计数
         * @param status 用户状态
         * @param employeeName 员工姓名
         * @param sex 员工性别
         * @param telephoneNumber 电话号码
         * @param email Email
         * @param workAreaUuid 工作区域uuid
         * @param workAreaSelectNamespace 查询工作区域数据的名空间
         * @param orderOffset 排序偏移
         * 
         * @return 1 添加成功<br />
         *         -1 用户名已存在<br />
         *         -2 工作区域不存在<br />
         *         -3 添加用户失败<br />
         *         -4 添加员工失败<br />
         *         0 未知错误<br />
         */
        // public int inline_addUserEmployee(SqlSession s, String userUuid, String userName, String password, String wechatOpenid, String role, Integer failedRetryCount, Integer status, String employeeName, String sex, String telephoneNumber, String email, String workAreaUuid, String workAreaSelectNamespace, Integer orderOffset) {
        // try {
        // // [内部变量声明]
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // int res = 0;
        // // [开始逻辑判断]
        // // 检查用户名是否已存在
        // p = new HashMap<String, Object>();
        // p.put("name", userName);
        // list = s.selectList("lego_user.selectUserSecurity", p);
        // if (DatabaseKit.hasData(list)) {
        // // 用户名已存在
        // return -1;
        // }
        // if (null != workAreaUuid) {
        // // 检查工作区域编号是否存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", workAreaUuid);
        // list = s.selectList((String) workAreaSelectNamespace, p);
        // if (!DatabaseKit.hasData(list)) {
        // // 工作区域不存在
        // return -2;
        // }
        // }
        // // 添加用户
        // p = new HashMap<String, Object>();
        // p.put("uuid", userUuid);
        // p.put("name", userName);
        // p.put("password", password);
        // p.put("wechat_openid", wechatOpenid);
        // p.put("role", role);
        // p.put("failedRetryCount", failedRetryCount);
        // p.put("status", status);
        // res = s.insert("lego_user.insertUserSecurity", p);
        // if (1 > res) {
        // // 添加用户失败
        // return -3;
        // }
        // // 添加员工
        // p = new HashMap<String, Object>();
        // p.put("user_uuid", userUuid);
        // p.put("name", employeeName);
        // p.put("sex", sex);
        // p.put("telephone_number", telephoneNumber);
        // p.put("email", email);
        // p.put("work_area_uuid", workAreaUuid);
        // p.put("order_offset", orderOffset);
        // res = s.insert("lego_workflow.insertEmployee", p);
        // if (1 > res) {
        // // 添加员工失败
        // return -4;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 修改工作流作业
         * 
         * @param s 执行SqlSession
         * @param uuid 作业uuid
         * @param process_uuid 工作流编号
         * @param content 作业内容
         * @param position_offset 当前审批岗位偏移
         * @param creator_uuid 创建者uuid
         * @param status 审批状态
         * @return 1 修改成功<br />
         *         -1 没有找到工作流数据<br />
         *         -2 没有找到工作流附加的文件集群<br />
         *         -3 修改工作流作业出错<br />
         *         0 未知错误<br />
         */
        // public int inline_modifyTask(SqlSession s, String uuid, String process_uuid, Object content, Object position_offset, String creator_uuid, Object status) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // // 检查process_uuid是否已存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", process_uuid);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -1;
        // }
        // // 新增工作流
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // p.put("process_uuid", process_uuid);
        // p.put("content", content);
        // p.put("position_offset", position_offset);
        // p.put("creator_uuid", creator_uuid);
        // p.put("status", status);
        // int res = s.update("lego_workflow.updateTask", p);
        // if (1 > res) {
        // return -3;
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 添加工作流记录
         * 
         * @param s 执行SqlSession
         * @param task_uuid 作业uuid
         * @param result 审批结果
         * @param suggestion 审批意见
         * @param creator_uuid 创建者uuid
         * 
         * @return 1 操作成功，工作流作业拒绝<br />
         *         2 操作成功，工作流作业同意<br />
         *         3 操作成功，工作流作业轮至下一岗位<br />
         *         -1 没有找到正在审批的作业<br />
         *         -2 没有找到作业所属的工作流<br />
         *         -3 作业的审批岗位超出流程设置范围<br />
         *         -4 没有找到当前审批的岗位信息<br />
         *         -5 没有找到当前审批岗位的员工信息<br />
         *         -6 当前用户没有审批工作流权限<br />
         *         -7 添加作业审批记录出错<br />
         *         -8 拒绝审批时，更新作业审批状态出错<br />
         *         -9 同意审批时，更新作业审批状态出错<br />
         *         -10 工作流审批轮至下一岗位时，更新作业审批岗位偏移出错<br />
         *         0 未知错误<br />
         */
        // public int inline_addRecord(SqlSession s, String task_uuid, Object result, Object suggestion, String creator_uuid) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // int res = 0;
        // // 记录所属作业的流程uuid
        // String process_uuid = null;
        // // 记录所属作业的岗位的偏移
        // Integer position_offset = null;
        // // 记录所属作业的当前的岗位
        // String current_position = null;
        // // 记录所属作业的下个的岗位
        // String next_position = null;
        // // 记录所属作业的流程中岗位审批的顺序
        // String position_list = null;
        // // 作业的uuid
        // String uuid = null;
        // // 检查作业编号是否存在
        // p = new HashMap<String, Object>();
        // p.put("uuid", task_uuid);
        // // 审批状态为“正在审批”
        // p.put("status", 1);
        // list = s.selectList("lego_workflow.selectTask", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -1;
        // }
        // // 根据作业编号获得流程编号和当前审批的岗位偏移
        // HashMap<String, Object> taskHm = list.iterator().next();
        // process_uuid = (String) taskHm.get("process_uuid");
        // position_offset = (Integer) taskHm.get("position_offset");
        // uuid = (String) taskHm.get("uuid");
        // // 获得该流程的审批岗位信息
        // p = new HashMap<String, Object>();
        // p.put("uuid", process_uuid);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -2;
        // }
        // HashMap<String, Object> processHm = list.iterator().next();
        // // 获得流程审批的岗位信息和抄送的岗位信息
        // position_list = (String) processHm.get("position_list");
        // String position_list_arr[] = position_list.split(";");
        // if (position_offset >= position_list_arr.length) {
        // return -3;
        // }
        // current_position = position_list_arr[position_offset];
        // // 根据当前岗位编号获得与之相关的岗位信息
        // p = new HashMap<String, Object>();
        // p.put("uuid", current_position);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -4;
        // }
        // HashMap<String, Object> positionHm = list.iterator().next();
        // String arr[] = ((String) positionHm.get("employee_list")).split(";");
        // boolean isValid = false;
        // // 用户编号转为员工编号
        // p = new HashMap<String, Object>();
        // p.put("user_uuid", creator_uuid);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // if (!DatabaseKit.hasData(list)) {
        // return -5;
        // }
        // // 判断当前添加记录的员工是否有权限操作
        // HashMap<String, Object> employeeHm = list.iterator().next();
        // String employee_uuid = (String) employeeHm.get("uuid");
        // // 遍历当前的岗位信息是否包括用户编号
        // for (int i = 0; i < arr.length; i++) {
        // if (employee_uuid.equalsIgnoreCase(arr[i])) {
        // isValid = true;
        // break;
        // }
        // }
        // if (!isValid) {
        // return -6;
        // }
        // // 如果用户操作合法插入工作流记录
        // p = new HashMap<String, Object>();
        // p.put("task_uuid", task_uuid);
        // p.put("result", result);
        // p.put("suggestion", suggestion);
        // p.put("creator_uuid", creator_uuid);
        // res = s.insert("lego_workflow.insertRecord", p);
        // if (1 > res) {
        // return -7;
        // }
        // // 当结果为拒绝（0）时结束当前作业
        // if (0 == ((Integer) result).intValue()) {
        // p = new HashMap<String, Object>();
        // p.put("uuid", task_uuid);
        // p.put("status", 3);
        // res = s.update("lego_workflow.updateTask", p);
        // if (1 > res) {
        // return -8;
        // } else {
        // if (1 != this.communication.inline_addMessage(s, null/* 没有附件时，directory_uuid为空 */, null/* 没有附件时，file_status为空 */, 3/* 消息类型（工作流审批消息） */, uuid/* 标题（uuid） */, 0/* 内容（审批结果：拒绝） */, 4/* 内容类型（通知消息） */, 0/* 发件人编号 */, String.valueOf(taskHm.get("creator_uuid")) + ";"/* 收件人uuid集合 */, null/* 抄送人编号集合 */, null/* 密送人编号集合 */, null/* 附件集群集合 */, 0/* 读取状态 */)) {
        // Framework.LOG.error("Send Workflow Disagree Message Error");
        // }
        // return 1;
        // }
        // }
        // // 判断当前流程的审批是否结束
        // if ((position_offset + 1) >= position_list_arr.length) {
        // // 标记作业状态为成功
        // p = new HashMap<String, Object>();
        // p.put("uuid", task_uuid);
        // p.put("status", 2);
        // res = s.update("lego_workflow.updateTask", p);
        // if (1 > res) {
        // return -9;
        // }
        // if (1 != this.communication.inline_addMessage(s, null/* 没有附件时，directory_uuid为空 */, null/* 没有附件时，file_status为空 */, 3/* 消息类型（工作流审批消息） */, uuid/* 标题（uuid） */, 1/* 内容（审批结果：同意） */, 4/* 内容类型（通知消息） */, 0/* 发件人编号 */, String.valueOf(taskHm.get("creator_uuid")) + ";"/* 收件人uuid集合 */, taskHm.get("cc_list")/* 抄送人编号集合 */, null/* 密送人编号集合 */, null/* 附件集群集合 */, 0/* 读取状态 */)) {
        // Framework.LOG.error("Send Workflow Agree Message Error");
        // }
        // return 2;
        // }
        // // 根据流程的岗位设置，更改作业的当前审批岗位的偏移
        // p = new HashMap<String, Object>();
        // p.put("uuid", task_uuid);
        // p.put("position_offset", position_offset + 1);
        // res = s.update("lego_workflow.updateTask", p);
        // if (1 > res) {
        // return -10;
        // }
        // // 设置下个岗位
        // next_position = position_list_arr[position_offset + 1];
        // // 获取下个岗位下的员工编号集合
        // p = new HashMap<String, Object>();
        // p.put("uuid", next_position);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // iter = list.iterator();
        // String nextUsers = "";
        // while (iter.hasNext()) {
        // positionHm = iter.next();
        // String employee_list[] = ((String) positionHm.get("employee_list")).split(";");
        // for (int i = 0; i < employee_list.length; i++) {
        // // 员工编号转为用户编号
        // p = new HashMap<String, Object>();
        // p.put("uuid", employee_list[i]);
        // list = s.selectList("lego_workflow.selectEmployee", p);
        // Iterator<HashMap<String, Object>> employeeIter = list.iterator();
        // while (employeeIter.hasNext()) {
        // employeeHm = employeeIter.next();
        // nextUsers += employeeHm.get("user_uuid") + ";";
        // }
        // }
        // }
        // if (1 != this.communication.inline_addMessage(s, null/* 没有附件时，directory_uuid为空 */, null/* 没有附件时，file_status为空 */, 3/* 消息类型（工作流审批消息） */, uuid/* 标题（uuid） */, 2/* 内容（审批结果：等待审批） */, 4/* 内容类型（通知消息） */, 0/* 发件人编号 */, nextUsers/* 收件人编号集合 */, null/* 抄送人编号集合 */, null/* 密送人编号集合 */, null/* 附件集群集合 */, 0/* 读取状态 */)) {
        // Framework.LOG.error("Send Workflow Notify Message Error");
        // }
        // return 3;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 获取工作流进程
         * 
         * @param s 执行SqlSession
         * @param uuid 工作流编号
         * @param department_uuid 部门编号
         * @param array 工作流数据返回JSONArray
         * @return 1 操作成功<br />
         *         2 没有找到工作流数据<br />
         *         0 未知错误<br />
         */
        // public int inline_getProcess(SqlSession s, String uuid, String department_uuid, JSONArray array) {
        // try {
        // HashMap<String, Object> p = null;
        // List<HashMap<String, Object>> list = null;
        // if ((null == uuid) && (null == department_uuid)) {
        // // 查询所有工作流
        // list = s.selectList("lego_workflow.selectProcess");
        // } else if ((null != uuid) && (null == department_uuid)) {
        // // 查询某个工作流
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // } else if ((null == uuid) && (null != department_uuid)) {
        // list = new ArrayList<HashMap<String, Object>>();
        // // 迭代返回所有当前部门和父部门的工作流
        // ArrayList<String> depList = new ArrayList<String>();
        // depList.add(department_uuid);
        // this.inline_getParentDepartment(s, depList, department_uuid);
        // Iterator<String> depIter = depList.iterator();
        // while (depIter.hasNext()) {
        // Object obj = depIter.next();
        // // 获取当前部门下的流程
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", obj);
        // List<HashMap<String, Object>> processList = s.selectList("lego_workflow.selectProcess", p);
        // Iterator<HashMap<String, Object>> processIter = processList.iterator();
        // while (processIter.hasNext()) {
        // list.add(processIter.next());
        // }
        // }
        // } else if ((null != uuid) && (null != department_uuid)) {
        // // 查询某个工作流
        // p = new HashMap<String, Object>();
        // p.put("uuid", uuid);
        // list = s.selectList("lego_workflow.selectProcess", p);
        // }
        // if (!DatabaseKit.hasData(list)) {
        // return 2;
        // }
        // Iterator<HashMap<String, Object>> iter = list.iterator();
        // while (iter.hasNext()) {
        // Iterator<Entry<String, Object>> iter2 = iter.next().entrySet().iterator();
        // JSONObject row = new JSONObject();
        // while (iter2.hasNext()) {
        // Entry<String, Object> e = iter2.next();
        // row.put(e.getKey(), e.getValue());
        // }
        // array.put(row);
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * （基于类内）获取部门下的员工数量
         * 
         * @param s 执行SqlSession
         * @param uuid 部门uuid
         * @param obj 返回的json对象
         * 
         * @return 1: 获取成功<br />
         *         -1: 获取子部门失败<br />
         *         0: 未知错误<br />
         */
        // public int inline_getEmployeeCount(SqlSession s, String uuid, HashMap<String, String> count) {
        // try {
        // HashMap<String, Object> p = null;
        // HashMap<String, Object> hm = null;
        // List<HashMap<String, Object>> list = null;
        // Iterator<HashMap<String, Object>> iter = null;
        // ArrayList<String> depList = null;
        // Iterator<String> depIter = null;
        // // 获取部门下的包括当前部门在内，以及所有子部门。
        // depList = new ArrayList<String>();
        // depList.add(uuid);
        // if (-2 == this.inline_getChildDepartment(s, depList, uuid)) {
        // return -1;
        // }
        // depIter = depList.iterator();
        // HashMap<String, String> employeeHm = new HashMap<String, String>();
        // while (depIter.hasNext()) {
        // Object depUuid = depIter.next();
        // // 获取部门下所有的岗位
        // p = new HashMap<String, Object>();
        // p.put("department_uuid", depUuid);
        // list = s.selectList("lego_workflow.selectPosition", p);
        // iter = list.iterator();
        // while (iter.hasNext()) {
        // hm = (HashMap<String, Object>) iter.next();
        // String employee_list = (String) hm.get("employee_list");
        // String employeeArr[] = employee_list.split(";");
        // for (int i = 0; i < employeeArr.length; i++) {
        // employeeHm.put(employeeArr[i], employeeArr[i]);
        // }
        // }
        // count.put("count", String.valueOf(employeeHm.size()));
        // }
        // return 1;
        // } catch (Exception e) {
        // Framework.LOG.warn(e.toString());
        // return 0;
        // }
        // }

        /**
         * 添加工作流岗位<br />
         * 
         * 参数列表所需参数：<br />
         * department_uuid: 部门uuid<br />
         * name: 岗位名称<br />
         * description: 岗位描述<br />
         * employee_list: 岗位包含的员工列表<br />
         * order_offset: 岗位排序偏移<br />
         */
        // public void c_addPosition() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // // [接收所需参数]
        // String department_uuid = (String) this.parameter.get("department_uuid");
        // String name = (String) this.parameter.get("name");
        // String description = (String) this.parameter.get("description");
        // String employee_list = (String) this.parameter.get("employee_list");
        // Integer order_offset = (Integer) this.parameter.get("order_offset");
        // // [开始逻辑判断]
        // int res = this.inline_addPosition(s, department_uuid, name, description, employee_list, order_offset);
        // switch (res) {
        // // 添加成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到与department_uuid对应的部门数据
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "没有找到与department_uuid对应的部门数据");
        // return;
        // // 部门下岗位名称已有重名
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_DUPLICATE_DATA, null, "部门下岗位名称已有重名");
        // return;
        // // 没有找到岗位里的员工信息
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "没有找到岗位里的员工信息");
        // return;
        // // 添加岗位失败
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "添加岗位失败");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "未知错误");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 修改工作流岗位<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 岗位编号<br />
         * department_uuid: 部门编号<br />
         * name: 岗位名称<br />
         * description: 岗位描述<br />
         * employee_list: 岗位员工列表<br />
         * order_offset: 排序偏移<br />
         * 
         * ERROR_1: 没有找到部门。<br />
         * ERROR_2: 部门下岗位名称已有重名。<br />
         * ERROR_3: 没有找到岗位设置的员工信息。<br />
         * ERROR_4: 未知错误。<br />
         */
        // public void c_modifyPosition() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_modifyPosition(s, (String) this.parameter.get("uuid"), (String) this.parameter.get("department_uuid"), (String) this.parameter.get("name"), (String) this.parameter.get("description"), (String) this.parameter.get("employee_list"), this.parameter.get("order_offset"));
        // switch (res) {
        // // 修改成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到部门
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 部门下岗位名称已有重名
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_DUPLICATE_DATA, null, "ERROR_2");
        // return;
        // // 没有找到岗位设置的员工信息
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_3");
        // return;
        // // 修改工作流岗位出错
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_4");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 取消工作流作业<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 作业编号<br />
         * creator_uuid: 创建者uuid<br />
         * status: 审批状态<br />
         * 
         * ERROR_1: 没有找到工作流数据。<br />
         * ERROR_2: 没有找到工作流附加的文件集群。<br />
         * ERROR_3: 修改工作流作业出错。<br />
         * ERROR_4: 未知错误。<br />
         */
        // public void c_cancelTask() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_modifyTask(s, (String) this.parameter.get("uuid"), null, null, null, (String) this.parameter.get("creator_uuid"), this.parameter.get("status"));
        // switch (res) {
        // // 修改成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到工作流数据
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 没有找到工作流附加的文件集群
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
        // return;
        // // 修改工作流作业出错
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_4");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 添加工作流流程<br />
         * 
         * 参数列表所需参数：<br />
         * department_uuid: 部门编号<br />
         * icon: 工作流图标<br />
         * name: 工作流名称<br />
         * description: 工作流描述<br />
         * content: 工作流内容<br />
         * position_list: 工作流审批岗位列表<br />
         * cc_list: 工作流抄送岗位列表<br />
         * status: 工作流状态<br />
         * 
         * ERROR_1: 没有找到对应部门。<br />
         * ERROR_2: 部门下工作流已有重名。<br />
         * ERROR_3: 工作流审批岗位信息没有找到。<br />
         * ERROR_4: 工作流员工抄送信息没有找到。<br />
         * ERROR_5: 添加工作流程出错。<br />
         * ERROR_6: 未知错误。<br />
         */
        // public void c_addProcess() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_addProcess(s, (String) this.parameter.get("department_uuid"), this.parameter.get("icon"), this.parameter.get("name"), this.parameter.get("description"), this.parameter.get("content"), (String) this.parameter.get("position_list"), (String) this.parameter.get("cc_list"), this.parameter.get("status"));
        // switch (res) {
        // // 添加成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到对应部门
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 部门下工作流已有重名
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_DUPLICATE_DATA, null, "ERROR_2");
        // return;
        // // 工作流审批岗位信息没有找到
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_3");
        // return;
        // // 工作流员工抄送信息没有找到
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_4");
        // return;
        // // 添加工作流程出错
        // case -5:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_5");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_6");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 修改工作流流程<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 工作流编号<br />
         * department_uuid: 部门编号<br />
         * icon: 工作流图标<br />
         * name: 工作流名称<br />
         * description: 工作流描述<br />
         * content: 工作流内容<br />
         * position_list: 工作流审批岗位列表<br />
         * cc_list: 工作流抄送岗位列表<br />
         * status: 工作流状态<br />
         * 
         * ERROR_1: 没有找到对应部门。<br />
         * ERROR_2: 部门下工作流已有重名。<br />
         * ERROR_3: 工作流审批岗位信息没有找到。<br />
         * ERROR_4: 工作流员工抄送信息没有找到。<br />
         * ERROR_5: 修改工作流程出错。<br />
         * ERROR_6: 未知错误。<br />
         */
        // public void c_modifyProcess() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_modifyProcess(s, (String) this.parameter.get("uuid"), (String) this.parameter.get("department_uuid"), this.parameter.get("icon"), this.parameter.get("name"), this.parameter.get("description"), this.parameter.get("content"), (String) this.parameter.get("position_list"), (String) this.parameter.get("cc_list"), this.parameter.get("status"));
        // switch (res) {
        // // 修改成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到对应部门
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 部门下工作流已有重名
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_DUPLICATE_DATA, null, "ERROR_2");
        // return;
        // // 工作流审批岗位信息没有找到
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_3");
        // return;
        // // 工作流员工抄送信息没有找到
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_4");
        // return;
        // // 修改工作流程出错
        // case -5:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_5");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_6");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）获取员工所在岗位和部门
         * 
         * 参数列表所需参数：<br />
         * uuid: 员工编号<br />
         * 
         * ERROR_1: 未知错误。<br />
         */
        // public void c_getEmployeePosition() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // JSONArray array = new JSONArray();
        // int res = this.inline_getEmployeePosition(s, array, (String) this.parameter.get("uuid"));
        // switch (res) {
        // // 操作成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, array.length(), array.toString());
        // return;
        // // 没有找到岗位的数据
        // case -1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, array.length(), array.toString());
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_1");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）获取工作流流程（包含从属部门关系）
         * 
         * 参数列表所需参数：<br />
         * uuid: 工作流编号<br />
         * department_uuid: 部门编号<br />
         * 
         * ERROR_1: 未知错误。<br />
         */
        // public void c_getProcess() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // JSONArray array = new JSONArray();
        // int res = this.inline_getProcess(s, (String) this.parameter.get("uuid"), (String) this.parameter.get("department_uuid"), array);
        // switch (res) {
        // // 操作成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, array.length(), array.toString());
        // return;
        // // 没有找到工作流数据
        // case 2:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, array.length(), array.toString());
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）获取部门下的员工数量
         * 
         * 参数列表所需参数：<br />
         * department_uuid: 部门编号<br />
         * 
         * ERROR_1: 获取子部门失败。<br />
         * ERROR_2: 未知错误。<br />
         */
        // public void c_getEmployeeCount() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // HashMap<String, String> hm = new HashMap<String, String>();
        // int res = this.inline_getEmployeeCount(s, (String) this.parameter.get("uuid"), hm);
        // switch (res) {
        // // 操作成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, 1, hm.get("count"));
        // return;
        // // 获取子部门失败
        // case 2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）添加工作流作业
         * 
         * 参数列表所需参数：<br />
         * uuid: uuid<br />
         * process_uuid: 工作流编号<br />
         * content: 作业内容<br />
         * cluster_list: 文件集群<br />
         * cc_list: 审批通过后抄送的员工（分号分隔的员工编号）<br />
         * creator_uuid: 创建者uuid<br />
         * status: 审批状态<br />
         * 
         * ERROR_1: 没有找到工作流数据。<br />
         * ERROR_2: 没有找到工作流附加的文件集群。<br />
         * ERROR_3: 保存附件为正式文件出错。<br />
         * ERROR_4: 工作流员工抄送信息没有找到。<br />
         * ERROR_5: 新增工作流作业出错。<br />
         * ERROR_6: 未知错误。<br />
         */
        // public void c_addTask() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_addTask(s, (String) this.parameter.get("uuid"), (String) this.parameter.get("process_uuid"), this.parameter.get("content"), this.parameter.get("cluster_list"), this.parameter.get("cc_list"), this.parameter.get("creator_uuid"), this.parameter.get("status"));
        // switch (res) {
        // // 执行成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到工作流数据
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 没有找到工作流附加的文件集群
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
        // return;
        // // 保存附件为正式文件出错
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 工作流员工抄送信息没有找到
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
        // return;
        // // 新增工作流作业出错
        // case -5:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_5");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_6");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）添加工作流作业
         * 
         * 参数列表所需参数：<br />
         * userUuid: 用户的uuid<br />
         * userName: 用户名<br />
         * password: 密码<br />
         * wechatOpenid: 微信的openid<br />
         * role: 角色<br />
         * failedRetryCount: 失败重试计数<br />
         * status: 用户状态<br />
         * employeeName: 员工姓名<br />
         * sex: 员工性别<br />
         * telephoneNumber: 电话号码<br />
         * email: Email<br />
         * workAreaUuid: 工作区域uuid<br />
         * workAreaSelectNamespace: 查询工作区域数据的名空间<br />
         * orderOffset: 排序偏移<br />
         */
        // public void c_addEmployee() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // // [接收所需参数]
        // String userUuid = (String) this.parameter.get("userUuid");
        // String userName = (String) this.parameter.get("userName");
        // String password = (String) this.parameter.get("password");
        // String wechatOpenid = (String) this.parameter.get("wechatOpenid");
        // String role = (String) this.parameter.get("role");
        // Integer failedRetryCount = (Integer) this.parameter.get("failedRetryCount");
        // Integer status = (Integer) this.parameter.get("status");
        // String employeeName = (String) this.parameter.get("employeeName");
        // String sex = (String) this.parameter.get("sex");
        // String telephoneNumber = (String) this.parameter.get("telephoneNumber");
        // String email = (String) this.parameter.get("email");
        // String workAreaUuid = (String) this.parameter.get("workAreaUuid");
        // String workAreaSelectNamespace = (String) this.parameter.get("workAreaSelectNamespace");
        // Integer orderOffset = (Integer) this.parameter.get("orderOffset");
        // // [开始逻辑判断]
        // int res = this.inline_addUserEmployee(s, userUuid, userName, password, wechatOpenid, role, failedRetryCount, status, employeeName, sex, telephoneNumber, email, workAreaUuid, workAreaSelectNamespace, orderOffset);
        // switch (res) {
        // // 添加成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 用户名已存在
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_DUPLICATE_DATA, null, "用户名已存在");
        // return;
        // // 工作区域不存在
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "工作区域不存在");
        // return;
        // // 添加用户失败
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "添加用户失败");
        // return;
        // // 添加员工失败
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "添加员工失败");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "未知错误");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * （基于调用）添加工作流记录
         * 
         * 参数列表所需参数：<br />
         * task_uuid: 作业编号<br />
         * result: 审批结果<br />
         * suggestion: 审批意见<br />
         * creator_uuid: 创建者uuid<br />
         * 
         * ERROR_1: 没有找到正在审批的作业。<br />
         * ERROR_2: 没有找到作业所属的工作流。<br />
         * ERROR_3: 作业的审批岗位超出流程设置范围。<br />
         * ERROR_4: 没有找到当前审批的岗位信息。<br />
         * ERROR_5: 没有找到当前审批岗位的员工信息。<br />
         * ERROR_6: 当前用户没有审批工作流权限。<br />
         * ERROR_7: 添加作业审批记录出错。<br />
         * ERROR_8: 拒绝审批时，更新作业审批状态出错。<br />
         * ERROR_9: 同意审批时，更新作业审批状态出错。<br />
         * ERROR_10: 工作流审批轮至下一岗位时，更新作业审批岗位偏移出错。<br />
         * ERROR_11: 未知错误。<br />
         */
        // public void c_addRecord() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_addRecord(s, (String) this.parameter.get("task_uuid"), this.parameter.get("result"), this.parameter.get("suggestion"), (String) this.parameter.get("creator_uuid"));
        // switch (res) {
        // // 操作成功，工作流作业拒绝
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 操作成功，工作流作业同意
        // case 2:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 操作成功，工作流作业轮至下一岗位
        // case 3:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 没有找到正在审批的作业
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 没有找到作业所属的工作流
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_2");
        // return;
        // // 作业的审批岗位超出流程设置范围
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 没有找到当前审批的岗位信息
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_4");
        // return;
        // // 没有找到当前审批岗位的员工信息
        // case -5:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_5");
        // return;
        // // 当前用户没有审批工作流权限
        // case -6:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_PERMISSION, null, "ERROR_6");
        // return;
        // // 添加作业审批记录出错
        // case -7:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_7");
        // return;
        // // 拒绝审批时，更新作业审批状态出错
        // case -8:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_8");
        // return;
        // // 同意审批时，更新作业审批状态出错
        // case -9:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_9");
        // return;
        // // 工作流审批轮至下一岗位时，更新作业审批岗位偏移出错
        // case -10:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_10");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_11");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 删除工作流部门<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 部门编号<br />
         * 
         * ERROR_1: 获取子部门失败。<br />
         * ERROR_2: 删除部门下流程失败。<br />
         * ERROR_3: 删除部门下岗位失败。<br />
         * ERROR_4: 删除部门失败。<br />
         * ERROR_5: 未知错误。<br />
         */
        // public void c_removeDepartment() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removeDepartment(s, (String) this.parameter.get("uuid"));
        // switch (res) {
        // // 删除成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 获取子部门失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.MODULE_NO_DATA, null, "ERROR_1");
        // return;
        // // 删除部门下流程失败
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 删除部门下岗位失败
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 删除部门失败
        // case -4:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_4");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_5");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 删除工作流人员<br />
         * 
         * 参数列表所需参数：<br />
         * user_uuid: 员工编号<br />
         * 
         * ERROR_1: 更新工作流程抄送列表失。<br />
         * ERROR_2: 删除员工失败。<br />
         * ERROR_3: 未知错误。<br />
         */
        // public void c_removeEmployee() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removeEmployee(s, (String) this.parameter.get("user_uuid"));
        // switch (res) {
        // // 删除成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 更新工作流程抄送列表失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 删除员工失败
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_3");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 删除工作流岗位<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 岗位编号<br />
         * 
         * ERROR_1: 更新工作流审批岗位列表失败。<br />
         * ERROR_2: 删除工作流审批岗位失败。<br />
         * ERROR_3: 未知错误。<br />
         */
        // public void c_removePosition() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removePosition(s, (String) this.parameter.get("uuid"));
        // switch (res) {
        // // 执行成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 更新工作流审批岗位列表失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 删除工作流审批岗位失败
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_3");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 删除工作流程<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 流程编号<br />
         * 
         * ERROR_1: 删除作业失败。<br />
         * ERROR_2: 删除流程失败。<br />
         * ERROR_3: 未知错误。<br />
         */
        // public void c_removeProcess() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removeProcess(s, (String) this.parameter.get("uuid"));
        // switch (res) {
        // // 删除成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 删除作业失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 删除流程失败
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_3");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 删除工作流记录<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 记录编号<br />
         * 
         * ERROR_1: 删除失败。<br />
         * ERROR_2: 未知错误。<br />
         */
        // public void c_removeRecord() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removeRecord(s, (String) this.parameter.get("uuid"));
        // switch (res) {
        // // 删除成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 删除失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_2");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }

        /**
         * 删除工作流程<br />
         * 
         * 参数列表所需参数：<br />
         * uuid: 作业编号<br />
         * 
         * ERROR_1: 删除记录失败。<br />
         * ERROR_2: 删除作业附件失败。<br />
         * ERROR_3: 删除作业失败。<br />
         * ERROR_4: 未知错误。<br />
         */
        // public void c_removeTask() {
        // try {
        // SqlSession s = this.dbHandler.getSqlSession(false);
        // try {
        // int res = this.inline_removeTask(s, (String) this.parameter.get("uuid"));
        // switch (res) {
        // // 删除成功
        // case 1:
        // s.commit();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.SUCCESS, null, null);
        // return;
        // // 删除记录失败
        // case -1:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_1");
        // return;
        // // 删除作业附件失败
        // case -2:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_2");
        // return;
        // // 删除作业失败
        // case -3:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.ERROR, null, "ERROR_3");
        // return;
        // // 未知错误
        // default:
        // s.rollback();
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.UNKNOWN, null, "ERROR_4");
        // return;
        // }
        // } catch (Exception e) {
        // s.rollback();
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // } finally {
        // s.close();
        // }
        // } catch (Exception e) {
        // if (Framework.DEBUG_ENABLE) {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, e.toString());
        // } else {
        // Message.send(this.httpServletRequest, this.httpServletResponse, Message.STATUS.EXCEPTION, null, null);
        // }
        // Framework.LOG.warn(e.toString());
        // }
        // }
}
