<template>
  <div class="app-container ai-page">
    <el-card class="hero" shadow="never">
      <div class="hero-title">AI 智能图书助手</div>
      <div class="hero-subtitle">可咨询图书推荐、馆藏搜索、借阅/归还规则。当前为本地规则 + 简单 NLP 模拟实现，便于验收现场离线演示。</div>
      <el-input v-model="question" class="question-input" placeholder="例如：有没有人工智能入门书籍？Java 初学者推荐什么？如何归还图书？" @keyup.enter.native="handleAsk">
        <el-button slot="append" type="primary" :loading="loading" @click="handleAsk">提问</el-button>
      </el-input>
      <div class="quick-list">
        <el-tag v-for="item in examples" :key="item" effect="plain" @click="askExample(item)">{{ item }}</el-tag>
      </div>
    </el-card>

    <el-card v-if="answer" class="answer-card">
      <div slot="header"><i class="el-icon-chat-dot-round" /> 智能回答</div>
      <p class="answer-text">{{ answer }}</p>
      <el-alert v-if="process" :title="process" type="info" :closable="false" show-icon />
    </el-card>

    <el-row v-if="recommendations.length" :gutter="18">
      <el-col v-for="book in recommendations" :key="book.bookid" :xs="24" :sm="12" :md="8" :lg="6">
        <el-card class="book-card" shadow="hover">
          <img v-if="book.bookimg" :src="book.bookimg" class="cover" alt="图书封面">
          <div class="book-title">{{ book.bookname }}</div>
          <div class="book-meta">作者：{{ book.bookauthor }}</div>
          <div class="book-meta">类型：{{ book.booktypename || '未分类' }}</div>
          <el-tag :type="book.isborrowed === 1 ? 'danger' : 'success'" size="mini">{{ book.isborrowed === 1 ? '已借出' : '可借阅' }}</el-tag>
          <p class="book-desc">{{ book.bookdesc }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { askAi } from '@/api/ai'

export default {
  name: 'AiAssistant',
  data() {
    return {
      question: '',
      answer: '',
      process: '',
      recommendations: [],
      loading: false,
      examples: ['推荐几本计算机入门书', '有没有科幻小说', '如何借阅和归还图书']
    }
  },
  methods: {
    askExample(text) {
      this.question = text
      this.handleAsk()
    },
    handleAsk() {
      if (!this.question || !this.question.trim()) {
        this.$message.warning('请输入问题后再提问')
        return
      }
      this.loading = true
      askAi(this.question.trim()).then(res => {
        if (res.code === 0) {
          this.answer = res.data.answer
          this.process = res.data.process
          this.recommendations = res.data.recommendations || []
        } else {
          this.$message.error(res.msg || 'AI 助手暂时无法回答')
        }
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.ai-page { background: #f5f7fb; min-height: calc(100vh - 84px); }
.hero { border: 0; border-radius: 16px; margin-bottom: 18px; background: linear-gradient(135deg, #ecf5ff 0%, #ffffff 52%, #f0f9eb 100%); }
.hero-title { font-size: 30px; font-weight: 700; color: #1f2d3d; margin-bottom: 10px; }
.hero-subtitle { color: #5f6f89; line-height: 1.8; margin-bottom: 18px; }
.question-input { max-width: 900px; }
.quick-list { margin-top: 14px; }
.quick-list .el-tag { margin-right: 10px; cursor: pointer; }
.answer-card { margin-bottom: 18px; border-radius: 14px; }
.answer-text { font-size: 16px; line-height: 1.9; color: #303133; }
.book-card { margin-bottom: 18px; border-radius: 14px; min-height: 430px; }
.cover { width: 100%; height: 210px; object-fit: cover; border-radius: 10px; background: #eef2f7; }
.book-title { margin-top: 12px; font-size: 17px; font-weight: 700; color: #303133; }
.book-meta { margin-top: 7px; color: #606266; font-size: 13px; }
.book-desc { margin-top: 10px; color: #606266; line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 4; -webkit-box-orient: vertical; overflow: hidden; }
</style>
