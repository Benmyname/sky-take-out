1.使用mybatis-plus 重构service//存在mp未充分掌握的问题
可行性分析

优势：
- 减少约60%的CRUD代码（insert、update、delete、selectById等方法可以直接使用BaseMapper）
- 内置分页插件，比PageHelper更轻量
- LambdaQueryWrapper让动态查询更优雅，类型安全
- 自动填充createTime、updateTime等字段
- 完全兼容MyBatis，可以渐进式迁移

需要注意：
- 复杂的联表查询和动态SQL仍需保留XML或自定义方法
- PageHelper的分页需要改为MyBatis-Plus的Page
- 实体类需要添加@TableName、@TableId等注解
- 需要全面回归测试

迁移建议

渐进式迁移步骤：
1. 添加MyBatis-Plus依赖，移除MyBatis和PageHelper
2. 配置MyBatis-Plus（分页插件、自动填充等）
3. Mapper接口继承BaseMapper<Entity>
4. 删除简单CRUD方法，使用BaseMapper提供的
5. 复杂查询保留或改用QueryWrapper
6. Service层可选继承ServiceImpl<Mapper, Entity>进一步简化

示例对比：
// 重构前
@Delete("delete from address_book where id = #{id}")
void deleteById(Long id);


2.使用redis重构，提高该项目高并发能力