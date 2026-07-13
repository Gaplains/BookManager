import request from '@/utils/request'

export function askAi(question) {
  return request({
    url: '/ai/ask',
    method: 'get',
    params: { question }
  })
}
