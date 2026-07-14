# BookManager 图书管理系统

面向 Web 高级程序设计实训验收的前后端分离图书管理系统，覆盖用户注册登录、角色权限、图书信息管理、图书分类管理、借阅归还、借阅记录查询、用户管理、库存状态管理和 AI 智能图书助手。

## 一、项目结构

```text
BookManager/
├── BookManager2-master/                 # 后端 Spring Boot + MyBatis
│   ├── src/main/java/com/wangpeng/bms/
│   │   ├── web/                         # Controller 接口层
│   │   ├── service/                     # Service 业务层
│   │   ├── mapper/                      # MyBatis Mapper
│   │   ├── model/                       # 实体类
│   │   ├── config/                      # CORS、拦截器配置
│   │   └── interceptor/                 # 登录和角色拦截
│   ├── src/main/resources/application.properties
│   └── book_manager.sql                 # Navicat 可导入的 MySQL 初始化脚本
├── BookManagerVue-permission-control/   # 前端 Vue2 + Element UI
│   ├── src/api/                         # RESTful API 封装
│   ├── src/views/                       # 页面模块
│   ├── src/router/                      # 路由与权限菜单
│   └── src/store/                       # Vuex 登录状态
└── docs/                                # 实训交付说明文档
```

## 二、技术栈

| 层级 | 技术 |
| --- | --- |
| 后端 | Spring Boot 2.5.6、MyBatis、MySQL、Maven、Java 8 |
| 前端 | Vue 2、Vue Router、Vuex、Element UI、Axios |
| 数据库 | MySQL 5.7/8.0，推荐使用 Navicat 导入 SQL |
| 接口风格 | 前后端分离 REST API，统一前缀 `http://localhost:8092/BookManager/` |
| AI 功能 | 本地规则 + 简单 NLP 模拟的智能图书助手，可扩展为通义千问、DeepSeek、OpenAI API、Dify/Coze |
| 开发工具 | IntelliJ IDEA、Navicat、Node.js、Maven、Git |

## 三、运行环境

1. JDK 1.8。
2. Maven 3.6+。
3. Node.js 14/16 更适合 Vue CLI 4 项目。
4. MySQL 5.7 或 8.0。
5. IntelliJ IDEA 打开后端目录 `BookManager2-master`。
6. Navicat 新建数据库并导入 `BookManager2-master/book_manager.sql`。

## 四、数据库初始化（Navicat）

1. 打开 Navicat，连接本机 MySQL。
2. 新建数据库：`book_manager`，字符集建议 `utf8mb4`。
3. 右键数据库选择“运行 SQL 文件”。
4. 选择 `BookManager2-master/book_manager.sql` 并执行。
5. 检查至少存在 4 张核心表：`user`、`book_type`、`book_info`、`borrow`。

## 五、后端启动说明（IDEA）

1. 用 IDEA 打开 `BookManager2-master`。
2. 修改数据库配置文件：`BookManager2-master/src/main/resources/application.properties`。
   - `spring.datasource.url=jdbc:mysql://localhost:3306/book_manager?useAffectedRows=true`
   - `spring.datasource.username=root`
   - `spring.datasource.password=你的MySQL密码`
3. 等待 Maven 依赖下载完成。
4. 运行启动类：`com.wangpeng.bms.BookManagerApplication`。
5. 后端默认端口：`8092`，上下文路径：`/BookManager`。
6. 健康验证示例：访问 `http://localhost:8092/BookManager/bookInfo/getCount`，能返回数字表示后端和数据库连接正常。

## 六、前端启动说明

```bash
cd BookManagerVue-permission-control
npm install
npm run dev
```

启动成功后访问前端提示的本地地址，通常为 `http://localhost:9528` 或 `http://localhost:8080`。前端接口基地址在 `src/utils/request.js` 中配置为 `http://localhost:8092/BookManager/`。
如改动后端代码，前端同步需先切换到目标路径然后npm run build:prod

## 七、测试账号

| 角色 | 用户名 | 密码 | 权限说明 |
| --- | --- | --- | --- |
| 管理员 | `admin` | `admin` | 图书、分类、用户、借阅记录管理，可代读者借阅 |
| 普通读者 | `李明` | `123456` | 查询图书、借阅图书、查看/归还个人借阅记录、使用 AI 助手 |
| 普通读者 | `wangpeng` | `123456` | 查询图书、借阅图书、查看/归还个人借阅记录、使用 AI 助手 |

## 八、主要功能

1. 用户注册与登录：支持普通用户注册，登录后按角色生成菜单。
2. 管理员后台：管理员可管理用户、图书分类、图书信息与借阅记录。
3. 图书信息 CRUD：支持新增、查询、修改、删除、批量删除和封面上传。
4. 图书分类管理：支持分类增删改查，便于图书归档。
5. 图书借阅与归还：读者可借阅未借出的图书，并在借阅记录中完成归还。
6. 借阅记录查询：按用户、图书分页查询借阅数据。
7. 库存状态管理：图书借出后显示“已借出”，归还后恢复“未借出”。
8. AI 智能图书助手：自然语言咨询图书推荐、馆藏搜索、借阅规则。

## 九、AI 功能说明

本项目新增“AI 智能助手”菜单，后端接口为：

```text
GET /BookManager/ai/ask?question=有没有科幻小说
```

### 输入

用户输入自然语言问题，例如：

- “推荐几本计算机入门书”
- “有没有科幻小说”
- “如何借阅和归还图书”

### 处理过程

当前实现为可离线演示的模拟 AI，使⽤本地规则 + 简单 NLP 模拟实现。未实际接入api：

1. 读取用户问题并做关键词拆分。
2. 从图书名称、作者、分类、简介中匹配关键词。
3. 优先返回馆藏图书和可借阅状态。
4. 如果识别到借阅、归还、规则等意图，则返回图书馆使用规则说明。
5. 前端展示“回答 + 匹配过程 + 推荐图书卡片”。

### 输出

接口返回统一 JSON，包括回答文本、推荐图书列表和模拟 AI 处理说明。

### 扩展方案

后续可将 `AiAssistantController` 中的规则匹配替换为大模型 API：通义千问、DeepSeek、OpenAI API，或接入 Dify/Coze 智能体。建议保留当前接口格式，这样前端无需改动。

## 十、完整演示流程建议

1. 启动 MySQL，导入 SQL。
2. 启动 Spring Boot 后端。
3. 启动 Vue 前端。
4. 使用管理员 `admin/admin` 登录。
5. 演示新增图书分类、添加图书、编辑图书、查看用户。
6. 使用读者账号登录，搜索图书并借阅。
7. 在借阅信息管理中查看借阅记录并归还图书。
8. 进入 AI 智能助手，提问“推荐几本计算机入门书”与“如何归还图书”。

## 十一、小组成员分工

| 成员 | 分工 |
| --- | --- |
| 门佳乐 | 后端 Controller/Service/Mapper、数据库设计、接口调试、Vue 页面、Element UI 美化、权限菜单、前后端联调 |
| 王浩淼 | AI 功能设计、README/需求文档/用户手册/PPT、测试与演示视频 |

## 十二、AI 辅助开发记录

| 使用工具 | 参与环节 | 生成/辅助内容 | 人工修改与验证 |
| --- | --- | --- | --- |
| Codex / ChatGPT | 需求拆解、代码修改、文档撰写 | AI 助手接口、前端 AI 页面、README 启动说明 | 人工检查接口路径、角色权限、页面交互，并运行 Maven/前端构建验证 |
| deepseek | Bug 排查，解决调试过程中的各种疑难杂症 | 修复管理员代用户借阅表单字段绑定问题 | 确认用户选择框绑定 `userid`，避免借阅参数错误 |
| AI 文档助手 | 验收材料整理 | 生成验收流程、AI 功能说明、小组分工模板 | 根据本项目实际目录和账号补充修改 |

AI 输出不足：模拟 AI 不能真正理解复杂语义，推荐结果依赖关键词匹配。人工改进：增加借阅规则意图识别、推荐过程说明和未来大模型扩展方案。

## 十三、常见问题

1. **前端登录报网络错误**：确认后端已启动，端口为 `8092`，前端 `src/utils/request.js` 的 baseURL 未被修改。
2. **数据库连接失败**：确认 Navicat 中数据库名为 `book_manager`，并修改 `application.properties` 的用户名和密码。
3. **借阅失败**：确认图书状态为“未借出”，且用户 ID 有效。
4. **AI 助手无推荐**：尝试输入更明确关键词，如“Java”“历史”“科幻”“小说”。
5. **后端更改前端未同步**：前端需先切换到目标路径，通过npm run build:prod来更新