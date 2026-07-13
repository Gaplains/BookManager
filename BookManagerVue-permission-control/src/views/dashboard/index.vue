<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :xs="24" :md="16">
        <el-card class="welcome-card" shadow="hover">
          <div class="welcome-title">欢迎使用图书管理系统</div>
          <div class="welcome-subtitle">当前登录用户：{{ name }}（{{ roles[0] === 'admin' ? '管理员' : '读者' }}）</div>
          <el-divider />
          <el-steps :active="4" finish-status="success" simple>
            <el-step title="查询图书" />
            <el-step title="提交借阅" />
            <el-step title="查看记录" />
            <el-step title="归还图书" />
          </el-steps>
          <div class="guide-grid">
            <div v-for="item in guideList" :key="item.title" class="guide-item">
              <i :class="item.icon" />
              <div>
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters(['id', 'name', 'roles']),
    guideList() {
      const common = [
        { title: '图书检索', desc: '按书名、作者、分类筛选馆藏图书，查看封面、简介与借阅状态。', icon: 'el-icon-search' },
        { title: '借阅记录', desc: '查看借阅时间、归还时间，完成借阅与归还业务闭环。', icon: 'el-icon-notebook-2' },
        { title: 'AI 智能助手', desc: '输入自然语言问题，获取图书推荐、馆藏匹配和借阅规则说明。', icon: 'el-icon-chat-dot-round' }
      ]
      if (this.roles[0] === 'admin') {
        return [
          { title: '后台管理', desc: '维护图书信息、图书分类、用户账号，并可代读者办理借阅。', icon: 'el-icon-s-tools' },
          ...common
        ]
      }
      return common
    }
  }
}
</script>

<style scoped>
.dashboard-container { padding: 24px; background: #f5f7fb; min-height: calc(100vh - 84px); }
.welcome-card, .tips-card { border: 0; border-radius: 18px; }
.welcome-card { background: linear-gradient(135deg, #ffffff 0%, #ecf5ff 100%); }
.welcome-title { font-size: 34px; font-weight: 700; color: #1f2d3d; }
.welcome-subtitle { margin-top: 12px; color: #5f6f89; font-size: 16px; }
.guide-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(230px, 1fr)); gap: 16px; margin-top: 22px; }
.guide-item { display: flex; gap: 14px; padding: 18px; border-radius: 14px; background: rgba(255,255,255,.86); box-shadow: 0 8px 20px rgba(64,158,255,.08); }
.guide-item i { font-size: 34px; color: #409eff; margin-top: 4px; }
.guide-item h3 { margin: 0 0 8px; color: #303133; }
.guide-item p { margin: 0; color: #606266; line-height: 1.7; }
.tips-card ul { margin: 0; padding-left: 20px; color: #606266; line-height: 2; }
</style>
