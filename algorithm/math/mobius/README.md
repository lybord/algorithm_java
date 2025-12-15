欧拉函数

1. 定义 
   $$
   \varphi(n) = \sum_{i = 1}^{n} \left[ \gcd(i, n) = 1 \right]
   $$

2. 性质
   $$
   \sum_{d | n} \varphi(d) = n
   $$



莫比乌斯函数

1. 定义
   $$
   \mu(n) = 
   
   \begin{cases}
   
   1 & n = 1 \\
   
   (-1)^s & n = p_1p_2...p_s \\
   
   0 & n \text{包含相同质因子}
   
   \end{cases}
   $$

2. 性质
   $$
   \sum_{d|n} \mu(d) = [n = 1]
   $$

3. 联系
   $$
   \sum_{d|n} \mu(d) \frac{n}{d} = \varphi(n)
   $$
   

狄利克雷卷积

定义 $f(n), g(n)$ 是两个积性函数：
$$
(f * g) (n) = \sum_{d|n} f(d)g(\frac{n}{d}) = \sum_{d|n} f(\frac{n}{d})g(d)
$$
规律

1. 交换律：$f * g = g * f$
2. 结合律：$(f * g) * h = f * (g * h)$
3. 分配律：$(f + g) * h = f * h + g * h$

三个常用函数：

1. 元函数 $\varepsilon(n) = [n = 1]$
2. 常数函数 $1(n) = 1$
3. 恒等函数 $id(n) = n$

常用卷积关系：

1. 

$$
\sum_{d|n} \mu(d) = [n = 1] \iff \mu * 1 = \varepsilon
$$

2. 

$$
\sum_{u|d} \varphi(d) = n \iff \varphi * 1 = id
$$

3. 

$$
\sum_{d|n} \mu(d) \frac{n}{d} = \varphi(n) \iff \mu * id = \varphi
$$

4. 

   $$
   f * \varepsilon = f
   $$

5. 

   $$
   f * 1 \neq f
   $$
