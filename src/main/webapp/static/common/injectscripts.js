'use strict';

(function(window, document, undefined) {
    window.deliveryFlowStaticContent = {
        telephone_confirm_service: {
            ckhs: ['X总您好,<br>我是您的掌贝专属运营顾问xxx，感谢您选择我们的掌贝服务，后续掌贝系统上线、安装、培训的工作都是由我来全程负责。<br>请问您是本店的老板还是店长呢？<br>由于业务要求，对您所购买的掌贝套餐，这边需要跟您确认以下相关信息：您是否有营业执照以及行业资质？您是否有自己的微信公众号，您是否有自己的口碑门店，您需要选择那些支付方式？（微信、支付宝、银联）<br>感谢您的支持与配合，接下来我们会依据您所购买的掌贝套餐需要的资料进行收集，稍后我会建立一个商户服务群，需要您提供一下您的全名、电话号码以及微信号，我加下您，方便日后的对接。',
                'X总您好，我是您的专属掌贝运营顾问，感谢您启动掌贝服务；这是我们的服务群，给您提供下我的联系方式：xxxxxxxxxxx(电话号码），您有任何问题都可以与我联系，很高兴为您服务。群内第二段话术：<br>下面为您介绍下掌贝的服务流程：<br>1.需要您在1-2个工作日内通过我们的资料收集小程序提交开通掌贝后台及支付功能所需要的资料；<br>2.资料提交齐全后，开户顾问会帮您开通掌贝后台及支付功能，需要7—11个工作日时间；<br>3.开通后我会和您预约一个时间上门进行营销方案配置以及培训，主要培训内容为：店内经营分析、智能POS机使用、店内运营方案培训、财务对账；<br>4.在我上门培训过程中，依据您的营销方案会有相对应的物料设计图输出，我们会在上门后的第8-10个工作日内将制作好的物料成品快递到您店内；<br>5.后续我会给您发送一份服务流程介绍，请您注意查收<br>6.同时策划专家会与你沟通投放上线的内容及具体的节点时间。（聚引客）<br>第三段话术：<br>现在我邀请我们的开户顾问入群，他会负责您的掌贝后台开通和支付开通服务'
            ],
            tips: '特别提醒：商户在购买掌贝服务之前已有微信支付的情况下，我们依然默认重新帮商户开通微信支付功能。理由如下：所有使用掌贝服务的商户，必须使用掌贝开通的微信支付功能。<br>特别提醒：商户在购买掌贝服务之前已有支付宝口碑门店的情况下，我们不再重新帮商户开通支付宝口碑门店功能。但是需要商户将支付宝授权掌贝，再将口碑门店绑定商户后台',
            contactShopMemo: [
                {
                    text: '请在接到订单的一个工作日内联系商户',
                    val: 'contactShopMemo1'
                },
                {
                    text: '告知商户服务启动',
                    val: 'contactShopMemo2'
                },
                {
                    text: '确认并记录商户联系人角色，姓名以及电话号码、微信',
                    val: 'contactShopMemo3'
                },
                {
                    text: '确认商户资质是否齐全',
                    val: 'contactShopMemo4'
                },
                {
                    text: '确认商户是否有微信公众账号及口碑门店',
                    val: 'contactShopMemo5'
                }
            ],
            shopServiceGroupMemo: [
                {
                    text: '建立商户联系群，拉入相关人员：运营经理、策划专家（聚引客）、商户负责人',
                    val: 'shopServiceGroupMemo1'
                },
                {
                    text: '通知商户负责人，拉入相关人员：运营人员、店长',
                    val: 'shopServiceGroupMemo2'
                },
                {
                    text: '群内自我介绍+服务流程介绍',
                    val: 'shopServiceGroupMemo3'
                },
                {
                    text: '将开户顾问邀请入群',
                    val: 'shopServiceGroupMemo4'
                }
            ],
            flowOperator: [
                {
                    text: '完成建立商户服务',
                    val: 'flowOperator1'
                },
                {
                    text: '完成群内自我介绍和服务介绍',
                    val: 'flowOperator2'
                }
            ]
        },
        assign_order_handlers: {
            tipContent: {
                operationAdviser: '1、负责商户上门交付安装及培训服务<br>2、负责商户首次配置营销策划方案生成<br>3、负责商户售后维护工作',
                openAccountConsultant: '1、负责商户进件服务，包括商户资料收集微信公众号及支付功能开通、支付宝口碑账号开通、银联开通<br>2、负责商户支付售后维护工作',
                materialConsultant: '1、负责商户所有物料的下单及跟踪<br>2、负责通知运营顾问物料制作完成及到店'
            }
        },
        into_material_collection: {
            rqscr: pageDataObject.uploadfileurl+'/upload/avatar/xcx_ewm.jpg?v='+pageDataObject.staticVersion,
            ckhs: '第一段话术：<br>X总您好，感谢您选择我们掌贝服务，我是掌贝开户顾问XXX，负责咱们商户资料提交、后台和支付功能开通这块。<br>第二段话术：<br>X总您好，这个是您购买掌贝套餐资料收集小程序，为了快速帮您开通掌贝后台以及支付功能，烦请您登陆小程序账号，按照相关提示与要求提交所需资料喔，如有问题可以随时与我沟通。我的电话号码是：xxxxxxxxxxx<br>您的小程序账号：xxxx,默认密码：xxxx',
            czzy: '1、将掌贝智慧服务中心（资料收集小程序）二维码发送至商户群<br>2、附上商户账号和密码<br>3、商户提交的资料：<br>①客常来套餐<br>#联系人、联系电话、门店客服电话、重要邮箱、商铺logo照片（作掌贝后台头像）<br>#开户许可证或银行行印鉴卡照片（个体户无需提供）、法人银行卡正反面（反面需法人签名）、银行卡开户行、行号<br>#营业执照、法人身份证正反面、法人手持身份证正面半身照<br>#行业资质证明（如餐饮服务许可证、食品流通许可证、食品卫生许可证）<br>#店外门面照、店内环境照、收银台照各2张，共6张<br>#微信公众号账号、密码<br>②聚引客套餐<br>#联系人、联系电话、门店客服电话、重要邮箱、法人身份证正反面、商铺logo照片（作掌贝后台头像）<br>#企业请提供：开户许可证或银行行印鉴卡照片；个体户请提 供：法人银行卡正反面（反面需法人签名）、银行卡开户行、行号<br>#店外门面照、店内环境照、收银台照各2张，共6张<br>#营业执照、行业资质证明（如餐饮服务许可证、食品流通许可证、食品卫生许可证）<br>#微博账号、密码；微信公众号账号、密码<br>#用于广告的图片素材要求：<br>至少20张（要包括所有的产品），包括产品图、多品图、门店环境图等推广宣传素材，要求明亮清晰，具备广告操作效果<br>③客常来+聚引客套餐<br>#联系人、联系电话、门店客服电话、重要邮箱、商铺logo照片（作掌贝后台头像）<br>#开户许可证或银行行印鉴卡照片（个体户无需提供）、法人银行卡正反面（反面需法人签名）、银行卡开户行、开户支行<br>#营业执照、法人身份证正反面、法人手持身份证正面半身照<br>#行业资质证明（如餐饮服务许可证、食品流通许可证、食品卫生许可证）<br>#公众号账号密码、微博账号密码<br>#店外门面照、店内环境照、收银台照各2张，共6张<br>#用于广告的图片素材要求：<br>至少20张（要包括所有的产品），包括了了：产品图、多品图、门店环境图、等推广宣传素材，要求明亮清晰，具备⼴广告操 作效果<br>特别提醒：商户在购买掌贝服务之前已有微信支付的情况下，我们依然默认重新帮商户开通微信支付功能。理由如下：所有使用掌贝服务的商户，必须使用掌贝开通的微信支付功能。<br>特别提醒：商户在购买掌贝服务之前已有支付宝口碑门店的情况下，我们不再重新帮商户开通支付宝口碑门店功能。但是需要商户将支付宝授权掌贝，再将口碑门店绑定商户后台'
        },
        alipay_public_praise_apply: {
            alipayPublicPraise: [
                {
                    text: '支付宝宝申请',
                    val: 'alipayPublicPraise1'
                },
                {
                    text: '注册口碑门店',
                    val: 'alipayPublicPraise2'
                },
                {
                    text: '支付宝授权掌贝',
                    val: 'alipayPublicPraise3'
                },
                {
                    text: '掌贝后台绑定口碑门店',
                    val: 'alipayPublicPraise4'
                }
            ],
            cwlink: [
                {
                    url: 'https://help.koubei.com/',
                    text: '支付宝申请教程'
                },
                {
                    url: 'https://help.koubei.com/',
                    text: '注册口碑门店教程'
                },
                {
                    url: 'https://help.koubei.com/',
                    text: '支付宝授权掌贝教程'
                },
                {
                    url: 'https://help.koubei.com/',
                    text: '掌贝后台绑定口碑门店教程'
                }
            ]
        },
        zhangbei_account_open: {
            //任务操作指引
            czzy: '1、对商户在小程序上提交的资料进行审核，确保资料的完整性和正确性（若商户提交的资料不齐全，请与商户沟通并协助商户补全）<br>2、进行掌贝进件提审',
            //话术参考
            hsck: '不齐全的话术：<br>X总您好，这边后台看到您的资料提交（具体事项）还没完善喔，因为后续资料审核需要几个工作日，为了不影响您尽早使用掌贝产品，烦请尽快上传相关资料喔。若有哪些不懂，可跟我提出，我好协助您解决，合力完成资料录入喔。<br>齐全的话术<br>X总您好，感谢您的配合，我这边会马上将您提供的掌贝账号开通申请资料进行系统录入并提交审核，涉及后台开通及支付功能开通，需要8个工作日左右，请知晓。我这边首先会帮您开通掌贝后台，一般后台开通1/2个工作日可完成。'
        },
        public_number_open: {
            memo: [
                {
                    text: '预约商户时间',
                    val: 'memo1'
                },
                {
                    text: '公众号申请开通',
                    val: 'memo2'
                },
                {
                    text: '公众号后台打印认证公函，商户填写公函后提交认证',
                    val: 'memo3'
                },
                {
                    text: '通知商户留意接听微信官方客服电话，确认开通信息',
                    val: 'memo4'
                },
                {
                    text: '完成开通',
                    val: 'memo5'
                }
            ],
            remarks: '1、登录商户微信公众号后台<br>2、右上角图标点击，选择账号详情-获取公众号名称<br>3、任务栏--开发--基本配置--公众号开发信息--开发者ID（APP ID）<br>4、右上角图标点击，选择账号详情-获取公众号公开信息'
        },
        unionpay_account_open: {
            text:[
                '对商户提交的银联支付资料进行审核，确保资料的完整性和正确性；若必须由商户提供的资料不齐全，请与商户沟通并协助商户补全：',
                '将商户银联支付的资料补全，并完成银联支付资料提交审核：'
            ],
            //任务操作备忘
            remark: '1、对商户在小程序上提交的资料进行审核，确保资料的完整性和正确性（若商户提交的资料不齐全，请与商户沟通并协助商户补全；）<br>2、进行掌贝进件提审<br>3、申请多机具号',
            //任务操作指引1
            czzyone: '业管进行具体操作',
            //任务操作指引2
            czzytow: '1、向商户获取后台账号和密码<br>1、登录商户后台<br>2、进入商家中心<br>3、在任务栏找到设备管理<br>4、点击银联机具编号--输入申请号的编号<br>5、提醒商户修改后台登录密码',
        },
        zhangbei_store_create: {
            showPw: false,
            //任务操作指引
            czzy: '1、业管审核资料通过后，会将申请好的商户主账号和密码以短信的形式发送给商户<br>2、开户顾问同商户要账号和密码<br>3、开户顾问登录商户后台<br>4、进入商家中心<br>5、在左侧任务栏找到门店管理，点击进入<br>6、点击创建门店--输入相关资料<br>7、记录门店账号和密码<br>8、将商户总账号及密码、各门店账号及密码同步给商户<br>9、提醒商户修改密码',
        },
        shop_info_collection: {
            czzy: '1、在行业相关网站上收集商户资料，例：大众点评网、美团网、糯米网、拉手网等相关行业网站<br>2、根据网站点评内容查询商户人均消费、店内环境等<br>3、根据商户所处位置，判定商户消费高峰日及时间段以及客流量<br>4、毛利率=（营业收入-营业成本）÷营业成本×100%',
            mallFormlist: ['消费高峰日及时段','店内是否有常态的排队','店内是否有餐桌、美容桌等','餐桌颜色','墙壁颜色','收银台颜色','平均单比消费金额X（选填）','毛利率（选填）']
        },
        tableFormRemark: '1. 掌贝为您提供首次、二次的免费上门服务，若您需要第三次及以上的上门服务，则需要收取额外费用。2. 原则上服务总时长不超过6个小时，如因商户原因拖延，将另外收取费用。',
        tableFormRemarkm: '该表格电话与商户确认好后完善发送给商户，让商户清楚当天的具体安排。本次为免费上门，之后上门服务需收费。',
        visit_service_subscribe_first: {
            hsck: '微信群：X总您好，我是您的掌贝专属运营顾问XXX，目前已完成了掌贝后台开通及微信支付功能配置，满足了首次上门培训的条件。我想XX时间电话跟您沟通下当天的安排，我先列了下当天的流程安排，标红部分是我们等下电话需要跟您沟通确认的，您先看下，我们等下电话沟通。<br>电话沟通：<br>您好，请问是XX吗？我是您的掌贝专属运营顾问xxx，您还记得吗？刚在微信联系过您的XXX。这次打电话的目的是跟您确定掌贝上门策划服务的安排，需要占用您大概10分钟时间，请问您现在方便吗？<br>咱们的掌贝后台开通和微信支付配置已经完成，但是由于这边上门培训的内容涉及到多个环节和需要店铺不同岗位的人参加，建议我们先确定下具体的安排后，最后再来定时间，您看可以吗？（开头问候，尽量引导商户按照我们的沟通流程走）<br>① 当天需要签署银联三联单和支付宝授权绑定，需要您提前准备好公司的公章、支付宝账户密码，这项是否有问题？<br>②这次上门非常重要，当天要现场商量配置我们的会员方案，涉及到需要决定会员注册优惠是首单立减多少或着是赠送价值多少的赠品券，积分抵多少现金，会员储值是按充值的金额等级送金额呢，还是送赠品券等类似的会员营销配置。而且本次的上门会员方案配置是包含在我们第一次免费上门的服务内容中的，如果这次上门无法配置完成，后期如再上门配置是需要收费的。所以需要有能够当场决策的人在场，想跟您确认下咱们店铺关于会员的优惠力度能够决策的人是谁呢？<br>好的，那请保证XXX当天必须要在场。<br>如不能到场，调整时间；如时间也无法协调，调整引导客户当场电话或提前沟通做授权<br>（告知商户方案配置具体做什么，有利于商户准确判断决策人；告知商户本次上门免费，下次收费，有利于提高商户的重视，提高当场配置方案成功率）<br>③咱们现场要配置我们的会员方案，需要用到会员卡名称、店铺Logo，而且现场配置好了后是无法修改的，会员卡名称比如星巴克的会员卡叫“星享卡”，您可以参考下。为了让您有更多的思考时间和到时候提高配置的效率，会员卡名称和店铺Log需要您在我上门前先提前准备一个，您看可以吗？<br>④上门策划的时间建议从XX点到XX点，持续XX小时，您这边需要XX点到XX点参加，另外，店长、收银、财务、服务员的时间安排您可以看下给您发送的流程安排，看下是否有需要调整的。<br>如时间不OK：电话沟通调整确定<br>如人员无法参加：因为这次培训是最全的，也为了不影响店铺的正常工作，内容是分版块的。而且本次的培训是包含在第一次免费上面的服务内容中的，如果本次由于不参加导致不明白，后续再上门培训就涉及到收费了，所以建议还是要过来参加<br>（提高现场培训成功率）<br>好的，那您看您哪天比较方便？我这边过去上门。<br>好的，那咱们时间就定在XX，需要您提前准备好以下资料：<br>①公司公章、支付宝账号密码<br>②XXX决策人当天要在场<br>③提前准备一个会员卡名称和店铺Logo<br>④确认店长、服务员、财务、收银的人准时到场<br>好的，最后需要强调下，麻烦您务必要把对应的资料和人提前通知准备到位，咱们第一次上门主要服务内容是配置方案和人员培训，第二次上门主要是铺设物料，且本次上门的服务内容都是免费的，如果当天由于资料未准备好或人员不在场，后期再上门是需要收费的，所以要辛苦您多费心了。<br>（电话最后要把需要商户准备的资料再重复一遍，并强调本次上门免费，后续需要收费，让商户重视！）<br>好的，那我稍后把上门的流程按照咱们沟通的完善下，发送到微信的服务项目群，您稍后确认下，有问题我们及时沟通调整，如没有我们就照安排执行。<br>（最终的流程必须发送微信群并要商户回复，流程已商户再微信群的确认回复为准）',
            czzy: '1、保存商户上门服务安排清单<br>2、下载上门服务安排清单<br>3、将下载的安排清单图发送至微信群<br>4、和商户再次确认安排内容以及上门时间',
        },
        material_deploy_service:{
        	rwczzy:'1、将物料部署视频发送给商户<br> 2、确认商户吸收并理解视频内容',
        },
        visit_service_subscribe_material: {
            czzy: '1、电话商户，确认商户是否收到物料<br>2、预约上门时间<br>3、确认商户参与培训人员名单<br>4、提醒商户需要准备的资料',
            hsck: 'X总您好，我是您的掌贝专属顾问XXX。这边供应商说我们的物料已经签收了，打电话给您是想跟您确认下第二次上门铺设物料和店铺人员物料实操培训的时间，需要大概占用您5分钟的时间，您现在方便吗？<br>为了提高培训的效果和效率，有以下几件事情需要跟您确认下：<br>①当天需要铺设物料和用物料做实操培训，所以我们物料目前是否已经放置在店铺了？<br>如已经在店铺：好的<br>如未放置在店铺：麻烦您到时候提前把物料拿到店铺<br>②实操培训的时间建议从XX点到XX点，持续XX个小时，需要XXX人员到场，麻烦您确认下在XX时间能够在场？<br>如时间不OK：电话沟通调整确定<br>如人员无法参加：因为这次培训时最全的，而且如果本次由于不参加导致不明白，后续再培训就涉及到收费了，所以建议还是要过来参加<br>好的，那您看您哪天比较方便？我这边过去上门。<br>好的，那稍后会把具体的安排发送到微信项目群，您稍后确认下，有问题我们及时沟通调整，如没有我们就照安排执行。',
            czsm: '1、保存商户二次上门服务安排清单<br>2、下载二次上门服务安排清单<br>3、将下载的安排清单图发送至微信群<br>4、和商户确认安排内容以及上门时间'
        },
        commonhscks: 'X总您好，我是您的掌贝专属顾问XXX。这边供应商说我们的物料已经签收了，打电话给您是想跟您确认下第二次上门铺设物料和店铺人员物料实操培训的时间，需要大概占用您5分钟的时间，您现在方便吗？<br>为了提高培训的效果和效率，有以下几件事情需要跟您确认下：<br>①当天需要铺设物料和用物料做实操培训，所以我们物料目前是否已经放置在店铺了？<br>如已经在店铺：好的<br>如未放置在店铺：麻烦您到时候提前把物料拿到店铺<br>②实操培训的时间建议从XX点到XX点，持续XX个小时，需要XXX人员到场，麻烦您确认下在XX时间能够在场？<br>如时间不OK：电话沟通调整确定<br>如人员无法参加：因为这次培训时最全的，而且如果本次由于不参加导致不明白，后续再培训就涉及到收费了，所以建议还是要过来参加<br>好的，那您看您哪天比较方便？我这边过去上门。<br>好的，那稍后会把具体的安排发送到微信项目群，您稍后确认下，有问题我们及时沟通调整，如没有我们就照安排执行。',
        commonhsck: 'X总您好，我是您的掌贝专属顾问XXX。这边供应商说我们的物料已经签收了，打电话给您是想跟您确认下第二次上门铺设物料和店铺人员物料实操培训的时间，需要大概占用您5分钟的时间，您现在方便吗？<br>为了提高培训的效果和效率，有以下几件事情需要跟您确认下：<br>①当天需要铺设物料和用物料做实操培训，所以我们物料目前是否已经放置在店铺了？<br>如已经在店铺：好的<br>如未放置在店铺：麻烦您到时候提前把物料拿到店铺<br>②实操培训的时间建议从XX点到XX点，持续XX个小时，需要XXX人员到场，麻烦您确认下在XX时间能够在场？<br>如时间不OK：电话沟通调整确定<br>如人员无法参加：因为这次培训时最全的，而且如果本次由于不参加导致不明白，后续再培训就涉及到收费了，所以建议还是要过来参加<br>好的，那您看您哪天比较方便？我这边过去上门。<br>好的，那稍后会把具体的安排发送到微信项目群，您稍后确认下，有问题我们及时沟通调整，如没有我们就照安排执行。'
    };
    
    window.deliveryFlowByDoorService = {
    	FMPS:{//首次营销策划服务
    		isSubtitleOne:false,
    		czzyUp:'',
    		czzyDown:deliveryFlowStaticContent.visit_service_subscribe_first.czzy,
    		hsck:deliveryFlowStaticContent.visit_service_subscribe_first.hsck,
    		serviceGoalCode:'1',
    		serviceTypeCode:'1',
    		
    		serviceGoalText:'1. 为商户策划并配置营销策划方案，助力提升店铺客流运营；\n2. 为店铺员工培训掌贝店铺营销工具的使用，提升店铺员工服务效率，创造更好的用户体验。',
			shopAttendeesText:'老板、运营负责人、财务、店长、服务员、收银员',
			shopPreparationInfoText:'1. 公司公章（签银联三联单）\n2. 口碑账户密码（掌贝后台绑定口碑）\n3. 会员卡名称、商户logo',
			remarksText:'1. 掌贝为您提供首次的免费的上门部署服务，若您需要第二次及以上的上门服务，则需要收取额外费用。\n2. 原则上服务总时长不超过6个小时，如因商户原因拖延，将另外收取费用。\n3.培训过程增加休息环节：在智能POS试用培训完后，现场休息5分钟；在财务收款对账培训完后，现场休息5分钟。',
			
			tableformWrapper:'首次营销策划上门服务表', 
			remarklabel: ['银联三联单','培训材料及试题','培训确认书','服务承诺书','掌贝使用手册（加小视频）','掌贝故障自助解决服务卡','演示物料','机具激活','软硬件测试'],
			channelType: [0,1,2,3,4,5,6,7,8],
    	},
    	JYK:{//聚引客交付服务
    		isSubtitleOne:true,
    		czzyUp:'1、电话商户预约上门时间<br>2、确认商户培训参与人员并记录<br>3、提醒商户需要准备的资料',
    		czzyDown:'1、保存商户上门服务安排清单<br>2、下载上门服务安排清单<br>3、将下载的安排清单图发送至微信群<br>4、和商户确认安排内容以及上门时间',
    		hsck:deliveryFlowStaticContent.commonhscks,
    		serviceGoalCode:'3',
    		serviceTypeCode:'1',
    		
    		serviceGoalText:'',
			shopAttendeesText:'老板、运营负责人、财务、店长、服务员、收银员',
			shopPreparationInfoText:'',
			remarksText:'',
			
			tableformWrapper:'聚引客上门服务表', 
			remarklabel: ['银联三联单','培训材料及试题','培训确认书','服务承诺书','掌贝使用手册（加小视频）','掌贝故障自助解决服务卡','机具激活','软硬件测试'],
			channelType: [0,1,2,3,4,5,6,7],
    	},
    	FMPS_BASIC:{//首次上门服务（基础版）
    		isSubtitleOne:false,
    		czzyUp:'',
    		czzyDown:deliveryFlowStaticContent.visit_service_subscribe_first.czzy,
    		hsck:deliveryFlowStaticContent.visit_service_subscribe_first.hsck,
    		serviceGoalCode:'7',
    		serviceTypeCode:'1',
    		
    		serviceGoalText:'1. 为店铺员工培训掌贝店铺营销工具的使用，提升店铺员工服务效率，创造更好的用户体验。',
			shopAttendeesText:'老板、运营负责人、财务、店长、服务员、收银员',
			shopPreparationInfoText:'1. 公司公章（签银联三联单）\n2. 口碑账户密码（掌贝后台绑定口碑）\n3. 会员卡名称、商户logo',
			remarksText:'1. 掌贝为您提供首次的免费的上门部署服务，若您需要第二次及以上的上门服务，则需要收取额外费用。\n2. 原则上服务总时长不超过6个小时，如因商户原因拖延，将另外收取费用。\n3.培训过程增加休息环节：在智能POS试用培训完后，现场休息5分钟；在财务收款对账培训完后，现场休息5分钟。',
    	
			tableformWrapper:'首次上门服务基础服务表', 
			remarklabel: ['银联三联单','培训材料及试题','培训确认书','服务承诺书','掌贝使用手册（加小视频）','掌贝故障自助解决服务卡','演示物料','机具激活','软硬件测试'],
			channelType: [0,1,2,3,4,5,6,7,8],
    	},
    	VC:{//上门收费
    		isSubtitleOne:false,
    		czzyUp:'',
    		czzyDown:deliveryFlowStaticContent.visit_service_subscribe_first.czzy,
    		hsck:deliveryFlowStaticContent.visit_service_subscribe_first.hsck,
    		serviceGoalCode:'6',
    		serviceTypeCode:'2',
			
			serviceGoalText:'1、投诉处理\n2、客情维护\n3、客户需求',
			shopAttendeesText:'老板、运营负责人、财务、店长、服务员、收银员',
			shopPreparationInfoText:'',
			remarksText:'1. 本次服务是您购买的售后上门培训服务，请您务必认真参加培训，如因贵方原因导致培训效果不佳，我们不予二次上门。如需再次上门，请您联系您的专属顾问。\n2. 原则上服务总时长不超过6个小时，如因商户原因拖延，将另外收取费用。',
    	
			tableformWrapper:'售后上门服务收费', 
			remarklabel: ['培训资料'],
			channelType: [0],
    	},
    	FMPS_M:{//物料实施服务
    		isSubtitleOne:true,
    		czzyUp:deliveryFlowStaticContent.visit_service_subscribe_material.czzy,
    		czzyDown:deliveryFlowStaticContent.visit_service_subscribe_material.czsm,
    		hsck:deliveryFlowStaticContent.visit_service_subscribe_material.hsck,
    		serviceGoalCode:'2',
    		serviceTypeCode:'1',
    		
    		serviceGoalText:'物料部署',
			shopAttendeesText:'运营负责人、店长、服务员',
			shopPreparationInfoText:'物料',
			remarksText:'1. 掌贝为您提供首次的免费的上门部署物料服务，若您需要第二次及以上的上门服务，则需要收取额外费用。\n2. 原则上服务总时长不超过6个小时，如因商户原因拖延，将另外收取费用。',
			
			tableformWrapper:'物料实施上门服务表', 
			remarklabel: ['物料确认'],
			channelType: [0],
    	},
    	ZHCT_FONT:{
    		serviceGoalCode:'9',
    		serviceTypeCode:'1',
    		
    		serviceGoalText:'1、为商户安装并交付智慧餐厅\n2、为商户进行智慧餐厅后台、厅厨通以及POS机操作培训',
			shopAttendeesText:'老板、店长、运营、服务员、收银员',
			shopPreparationInfoText:'打印机\n收银机\n网络',
			remarksText:'',
			
			tableformWrapper:'智慧餐厅安装交付服务表', 
			remarklabel: [
			    {
			    	label:'1、检查智慧餐厅安装工具包',
			    	children:[
			    	    '打印机IP修改工具',
			    	    '打印机驱动',
			    	    '厅厨通安装工具包',
			    	    '云打印客户端安装工具包新旧版本',
			    	    'chrome浏览器安装包'
			    	]
			    },{
			    	label:'2、确认培训资料',
			    	children:[
			    	    '智慧餐厅后台培训文档',
			    	    '厅厨通操作培训文档',
			    	    'APP操作培训文档'
			    	]
			    },{
			    	label:'3、确认商户菜单已经上传完整',
			    	children:[]
			    }
			],
			commonhscks:'XXX您好，我是您的掌贝专属运营顾问XXX。给您打电话的目的是跟您再确定下明天上门服务的事项，需要占用您大概2分钟时间，请问您现在方便吗？为了确保上门效果，有以下几件事需要再跟您提醒确认下：<br><span style="color:red;">依据之前调研好的打印机、收银机、网络情况再次确认</span><br>好的，那谢谢XXX，咱们明天XX时间见。'
    	}
    }
    
    
    
    var task_def_key = pageDataObject.taskDefKey || erpApp.getQueryString('taskDefKey');
    
    /* 贝蚁1.0流程  */
    var scriptsMap = {
        'management_diagnosis_marketing_planning_3.1': '/scripts/promotion/promotion.min.js?v=',
        'management_diagnosis_marketing_planning_3.2': '/scripts/promotion/promotion.min.js?v=',
        'visit_service_complete_jyk': '/scripts/bytaskflow/visit_service_complete_jyk.min.js?v=',
        'alipay_public_praise_apply': '/scripts/bytaskflow/alipay_public_praise_apply.min.js?v=',
        'assign_order_handlers': '/scripts/bytaskflow/assign_order_handlers.min.js?v=',//查看订单信息，指派订单处理人员
        'into_material_collection': '/scripts/bytaskflow/into_material_collection.min.js?v=',
        'material_make_follow': '/scripts/bytaskflow/material_make_follow.min.js?v=',
        'material_make_submit': '/scripts/bytaskflow/material_make_submit.min.js?v=',
        'material_progress_sync': '/scripts/bytaskflow/material_progress_sync.min.js?v=',
        'public_number_open': '/scripts/bytaskflow/public_number_open.min.js?v=',
        'visit_service_apply_jyk': '/scripts/bytaskflow/visit_service_apply_jyk.min.js?v=',
        'visit_service_apply_first': '/scripts/bytaskflow/visit_service_apply_first.min.js?v=',
        'shop_info_collection': '/scripts/bytaskflow/shop_info_collection.min.js?v=',
        'telephone_confirm_service': '/scripts/bytaskflow/telephone_confirm_service.min.js?v=',
        'train_service_record': '/scripts/bytaskflow/train_service_record.min.js?v=',
        'unionpay_account_open': '/scripts/bytaskflow/unionpay_account_open.min.js?v=',
        'unionpay_account_train': '/scripts/bytaskflow/unionpay_account_train.min.js?v=',
        'visit_service_remind_jyk': '/scripts/bytaskflow/visit_service_remind_jyk.min.js?v=',
        'visit_service_apply_material': '/scripts/bytaskflow/visit_service_apply_material.min.js?v=',
        'visit_service_complete_first': '/scripts/bytaskflow/visit_service_complete_first.min.js?v=',
        'visit_service_modify_material': '/scripts/bytaskflow/visit_service_modify_material.min.js?v=',
        'visit_service_complete_material': '/scripts/bytaskflow/visit_service_complete_material.min.js?v=',
        'visit_service_modify_first': '/scripts/bytaskflow/visit_service_modify_first.min.js?v=',
        'visit_service_modify_jyk': '/scripts/bytaskflow/visit_service_modify_jyk.min.js?v=',
        'visit_service_remind_material': '/scripts/bytaskflow/visit_service_remind_material.min.js?v=',
        'visit_service_review_first': '/scripts/bytaskflow/visit_service_review_first.min.js?v=',//上门服务预约审核(首次营销策划服务)
        'visit_service_review_jyk': '/scripts/bytaskflow/visit_service_review_jyk.min.js?v=',
        'visit_service_review_material': '/scripts/bytaskflow/visit_service_review_material.min.js?v=',
        'visit_service_subscribe_first': '/scripts/bytaskflow/visit_service_subscribe_first.min.js?v=',//电话预约上门服务(首次营销策划服务)
        'visit_service_subscribe_jyk': '/scripts/bytaskflow/visit_service_subscribe_jyk.min.js?v=',
        'zhangbei_store_create': '/scripts/bytaskflow/zhangbei_store_create.min.js?v=',
        'visit_service_subscribe_material': '/scripts/bytaskflow/visit_service_subscribe_material.min.js?v=',
        'wechat_account_open': '/scripts/bytaskflow/wechat_account_open.min.js?v=',
        'wechat_shop_configuration': '/scripts/bytaskflow/wechat_shop_configuration.min.js?v=',
        'zhangbei_account_open': '/scripts/bytaskflow/zhangbei_account_open.min.js?v=',
        'visit_service_remind_first': '/scripts/bytaskflow/visit_service_remind_first.min.js?v=',
        'visit_service_apply': '/scripts/bytaskflow/visit_service_apply.min.js?v=',
        'visit_service_review': '/scripts/bytaskflow/visit_service_review.min.js?v=',
        'visit_service_modify': '/scripts/bytaskflow/visit_service_modify.min.js?v=',
        'visit_service_remind': '/scripts/bytaskflow/visit_service_remind.min.js?v=',
        'visit_service_complete': '/scripts/bytaskflow/visit_service_complete.min.js?v=',
    };
    
    /* 贝蚁2.0流程  */
    Object.assign(scriptsMap,{
    	//优化查看订单信息，指派订单处理人员-3.3
        'assign_order_handlers_3.3': '/scripts/bytaskflow_3.3/assign_order_handlers_3.3.min.js?v=',
        
        //优化-物料制作跟踪
        'material_make_follow_first': '/scripts/bytaskflow_3.3/material_make_follow_first.min.js?v=',
        
        //新增物料更新服务流程-3.3
        'material_deploy_service_first': '/scripts/bytaskflow_3.3/material_deploy_service_first.min.js?v=',
        'material_deploy_service_update': '/scripts/bytaskflow_3.3/material_deploy_service_update.min.js?v=',
        
        'material_make_follow_update': '/scripts/bytaskflow_3.3/material_make_follow_update.min.js?v=',
        'material_progress_sync_update': '/scripts/bytaskflow_3.3/material_progress_sync_update.min.js?v=',
        
         //合并上门服务
        'visit_service_subscribe_public': '/scripts/bytaskflow_3.3/visit_service_subscribe_public.min.js?v=',
        'visit_service_apply_public': '/scripts/bytaskflow_3.3/visit_service_apply_public.min.js?v=',
        'visit_service_review_public': '/scripts/bytaskflow_3.3/visit_service_review_public.min.js?v=',
        'visit_service_modify_public': '/scripts/bytaskflow_3.3/visit_service_modify_public.min.js?v=',
        'visit_service_remind_public': '/scripts/bytaskflow_3.3/visit_service_remind_public.min.js?v=',
        'visit_service_complete_public': '/scripts/bytaskflow_3.3/visit_service_complete_public.min.js?v=',
    });
    
    /* 贝蚁2.1流程  */
    Object.assign(scriptsMap,{
        //智慧餐厅安装交付服务
        'visit_service_subscribe_zhct': '/scripts/bytaskflow_3.4/visit_service_subscribe_zhct.min.js?v=',
        'visit_service_apply_zhct': '/scripts/bytaskflow_3.4/visit_service_apply_zhct.min.js?v=',
        'visit_service_review_zhct': '/scripts/bytaskflow_3.4/visit_service_review_zhct.min.js?v=',
        'visit_service_modify_zhct': '/scripts/bytaskflow_3.4/visit_service_modify_zhct.min.js?v=',
        'visit_service_remind_zhct': '/scripts/bytaskflow_3.4/visit_service_remind_zhct.min.js?v=',
        'visit_service_complete_zhct': '/scripts/bytaskflow_3.4/visit_service_complete_zhct.min.js?v=',
        
        //智慧餐厅安装交付服务(老商户)
        'visit_service_subscribe_zhct_old': '/scripts/bytaskflow_3.4/visit_service_subscribe_zhct_old.min.js?v=',
        'visit_service_apply_zhct_old': '/scripts/bytaskflow_3.4/visit_service_apply_zhct_old.min.js?v=',
        'visit_service_review_zhct_old': '/scripts/bytaskflow_3.4/visit_service_review_zhct_old.min.js?v=',
        'visit_service_modify_zhct_old': '/scripts/bytaskflow_3.4/visit_service_modify_zhct_old.min.js?v=',
        'visit_service_remind_zhct_old': '/scripts/bytaskflow_3.4/visit_service_remind_zhct_old.min.js?v=',
        'visit_service_complete_zhct_old': '/scripts/bytaskflow_3.4/visit_service_complete_zhct_old.min.js?v=',
        
         //电话联系商户，确认服务内容（客常来+智慧餐厅）
        'telephone_confirm_service_zhct': '/scripts/bytaskflow_3.4/telephone_confirm_service_zhct.min.js?v=',
        
        //智慧餐厅菜单配置
        'zhct_menu_configuration': '/scripts/bytaskflow_3.4/zhct_menu_configuration.min.js?v=',
        //智慧餐厅菜单配置(老商户)
        'zhct_menu_configuration_old': '/scripts/bytaskflow_3.4/zhct_menu_configuration_old.min.js?v=',
        
        //查看订单信息,指派订单处理人员(老商户)
        'assign_order_handlers_zhct_old': '/scripts/bytaskflow_3.4/assign_order_handlers_zhct_old.min.js?v=',
    });
    
    if (Object.keys(scriptsMap).indexOf(task_def_key) !== -1) {
    	//1.0
//    	erpApp.loadScript('http://localhost:7777/bytaskflow/'+task_def_key+'.min.js');
    	//2.0
//    	var task_def_key = 'visit_service_review_zhct';
//    	console.log(task_def_key);
//    	erpApp.loadScript('http://localhost:7777/bytaskflow_3.4/'+task_def_key+'.min.js');
    	
//        erpApp.loadScript('http://localhost:7777/promotion/promotion.min.js');
    	
        erpApp.loadScript(ctxStatic+scriptsMap[task_def_key]+pageDataObject.staticVersion);
    }
})(this, document);