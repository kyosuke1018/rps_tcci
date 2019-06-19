/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.util;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcCusAddr;
import com.tcci.ec.entity.EcCustomer;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcMemberMsg;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderDetail;
import com.tcci.ec.entity.EcOrderMessage;
import com.tcci.ec.entity.EcOrderRate;
import com.tcci.ec.entity.EcOrderShipInfo;
import com.tcci.ec.entity.EcPayment;
import com.tcci.ec.entity.EcPrdType;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.entity.EcSeller;
import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.MemberTypeEnum;
import com.tcci.ec.facade.DeliveryFacade;
import com.tcci.ec.facade.EcCompanyFacade;
import com.tcci.ec.facade.EcFileFacade;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.facade.order.EcOrderRateFacade;
import com.tcci.ec.facade.order.EcOrderShipInfoFacade;
import com.tcci.ec.facade.product.EcProductFacade;
import com.tcci.ec.facade.store.EcStoreFacade;
import com.tcci.ec.vo.Company;
import com.tcci.ec.vo.CusAddr;
import com.tcci.ec.vo.Customer;
import com.tcci.ec.vo.DeliveryPlace;
import com.tcci.ec.vo.Member;
import com.tcci.ec.vo.MemberMsg;
import com.tcci.ec.vo.Order;
import com.tcci.ec.vo.OrderDetail;
import com.tcci.ec.vo.OrderMessage;
import com.tcci.ec.vo.OrderRate;
import com.tcci.ec.vo.OrderShipInfo;
import com.tcci.ec.vo.Payment;
import com.tcci.ec.vo.PrdType;
import com.tcci.ec.vo.Product;
import com.tcci.ec.vo.Seller;
import com.tcci.ec.vo.Shipping;
import com.tcci.ec.vo.Store;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import org.apache.commons.collections.CollectionUtils;


/**
 *
 * @author Kyle.Cheng
 */
@Named
@Stateless
public class EntityTransforVO {
    
    @EJB
    private EcOrderShipInfoFacade ecOrderShipInfoFacade;
    @EJB
    private EcOrderRateFacade ecOrderRateFacade;
    @EJB 
    protected EcFileFacade fileFacade;
    @EJB
    private EcProductFacade ecProductFacade;
    @EJB
    private EcOptionFacade ecOptionFacade;
    @EJB 
    protected EcCompanyFacade ecCompanyFacade;
    @EJB 
    protected EcStoreFacade ecStoreFacade;
    @EJB 
    protected DeliveryFacade deliveryFacade;
    
    @Resource(mappedName = "jndi/ec.config")
    protected Properties jndiConfig;
    
    private SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME);
    
    public Member memberTransfor(EcMember entity){
        Member vo = new Member();
        vo.setId(entity.getId());
        vo.setLoginAccount(entity.getLoginAccount());
        vo.setName(entity.getName());
        vo.setEmail(entity.getEmail());
        vo.setPhone(entity.getPhone());
        vo.setSellerApply(entity.isSellerApply());
        vo.setSellerApprove(entity.isSellerApprove());
        vo.setApplytime(entity.getApplytime());
        vo.setApprovetime(entity.getApprovetime());
        vo = ecOrderRateFacade.countRateByMember(vo);
        vo.setType(entity.getType());
        if(MemberTypeEnum.COMPANY.getCode().equals(entity.getType())){
            List<EcCompany> list = ecCompanyFacade.findByMainId(entity.getId(), "M");
            if(CollectionUtils.isNotEmpty(list)){
                EcCompany ecCompany = list.get(0);
//                vo.setCompany(this.companyTransfor(ecCompany));
                Company company = this.companyTransfor(ecCompany);
                
                //會員照片
                String url = jndiConfig.getProperty("url.prefix");
//                List<String> imageList = new ArrayList<>();
                List<Long> imageIdList = new ArrayList<>();
                String appUrl = url + "/ec-service/resources/image?oid=";
                List<EcFile> fileList = ecCompanyFacade.findImageByMember(entity.getId());
                if(CollectionUtils.isNotEmpty(fileList)){
                    company.setImageUrl(appUrl);
                    //String appUrl = "/ec"; // 手機自行補 http://host_or_ip/
                    //String imageUrl = appUrl + "/service/image?oid=" + file.getId();
                    for(EcFile file:fileList){
//                        String imageUrl = appUrl + file.getId();
//                        imageList.add(imageUrl);
                        imageIdList.add(file.getId());
                    }
                    
//                    company.setImageList(imageList);
                    company.setImageIdList(imageIdList);
                }
                
                vo.setCompany(company);
            }
        }
        return vo;
    }
    
    public Customer customerTransfor(EcCustomer entity){
        Customer vo = new Customer();
        vo.setId(entity.getId());
        vo.setMemberId(entity.getMember().getId());
        vo.setStoreId(entity.getStoreId());
        if(entity.getStoreId()!=null){
            String name = ecStoreFacade.find(entity.getStoreId()).getCname();
            vo.setStoreName(name);
        }
        vo.setCredits(entity.getCredits());
        if(entity.getApplyTime()!=null){
            vo.setApplyTime(sdf.format(entity.getApplyTime()));
        }

        return vo;
    }
    
    public Seller sellerTransfor(EcSeller entity){
        Seller vo = new Seller();
        vo.setId(entity.getId());
        if(entity.getMember()!=null){
            vo.setMember(this.memberTransfor(entity.getMember()));
//            Member member = new Member();
//            member.setId(entity.getMember().getId());
//            vo.setMember(member);
        }

        return vo;
    }
    
    public Order orderTransfor(EcOrder entity){
        return this.orderTransfor(entity, null);
    }
    public Order orderTransfor(EcOrder entity, Locale locale){
        Order order = new Order();
        order.setId(entity.getId());
        if(entity.getMember()!=null){
            order.setMember(this.memberTransfor(entity.getMember()));
        }
        if(entity.getStore()!=null){
            order.setStore(this.storeTransfor(entity.getStore()));
        }
        if(entity.getOrderDetails()!=null){
            List<OrderDetail> list = new ArrayList<>();
            for(EcOrderDetail detail:entity.getOrderDetails()){
                list.add(this.orderDetailTransfor(detail));
            }
            order.setOrderDetails(list);
        }
        if(entity.getOrderMessages()!=null){
            List<OrderMessage> list = new ArrayList<>();
            for(EcOrderMessage message:entity.getOrderMessages()){
                list.add(this.orderMessageTransfor(message));
            }
            order.setOrderMessages(list);
        }
        
        order.setOrderNumber(entity.getOrderNumber());
        order.setTotal(entity.getTotal());
        order.setSubTotal(entity.getSubTotal());
        order.setShippingTotal(entity.getShippingTotal());
        order.setMessage(entity.getMessage());
        order.setStatus(entity.getStatus());
//        order.setPaymentType(entity.getPaymentType());
        order.setPayStatus(entity.getPayStatus());
        order.setShipStatus(entity.getShipStatus());
        order.setCurrCode("RMB");//fix
        if(entity.getDeliveryDate()!=null){
            order.setDeliveryDate(sdf.format(entity.getDeliveryDate()));
        }
        if(locale!= null){
            order.setOrderStatusDisplayName(entity.getStatus().getDisplayName(locale));
            order.setPayStatusDisplayName(entity.getPayStatus().getDisplayName(locale));
            order.setShipStatusDisplayName(entity.getShipStatus().getDisplayName(locale));
        }else{
            order.setOrderStatusDisplayName(entity.getStatus().getDisplayName());
            order.setPayStatusDisplayName(entity.getPayStatus().getDisplayName());
            order.setShipStatusDisplayName(entity.getShipStatus().getDisplayName());
        }
        
//        order.setRecipient(entity.getRecipient());
//        order.setPhone(entity.getPhone());
//        order.setAddress(entity.getAddress());
//        if(entity.getShipping()!=null){
//            Shipping shipping = this.shippingTransfor(entity.getShipping());
//            order.setShipping(shipping);
//            
//            EcOrderShipInfo ecOrderShipInfo = ecOrderShipInfoFacade.findByOrder(entity);
//            if(ecOrderShipInfo!=null){
//                order.setShipInfo(this.orderShipInfoTransfor(ecOrderShipInfo));
//            }
//        }
        
        EcOrderShipInfo ecOrderShipInfo = ecOrderShipInfoFacade.findByOrder(entity);
        if(ecOrderShipInfo!=null){
            order.setShipInfo(this.orderShipInfoTransfor(ecOrderShipInfo));
            if(ecOrderShipInfo.getShipping()!=null){
                Shipping shipping = this.shippingTransfor(ecOrderShipInfo.getShipping());
                order.setShipping(shipping);
            }
        }
        
        if(entity.getPayment()!=null){
            Payment payment = this.paymentTransfor(entity.getPayment());
            order.setPayment(payment);
        }
        
        EcOrderRate ecOrderRate = ecOrderRateFacade.findByOrderId(entity.getStore().getId(), entity.getId());
        if(ecOrderRate!=null){
            OrderRate orderRate = new OrderRate();
            orderRate.setOrderId(entity.getId());
            orderRate.setStoreId(entity.getStore().getId());
            if(ecOrderRate.getCustomerRate()!=null){
                orderRate.setCustomerRate(ecOrderRate.getCustomerRate());
                orderRate.setCustomerMessage(ecOrderRate.getCustomerMessage());
            }
            if(ecOrderRate.getSellerRate()!=null){
                orderRate.setSellerRate(ecOrderRate.getSellerRate());
                orderRate.setSellerMessage(ecOrderRate.getSellerMessage());
            }
            order.setOrderRate(orderRate);
        }

        order.setCreatetime(entity.getCreatetime());
        order.setOriTotal(entity.getOriTotal());
        order.setBuyerCheck(entity.getBuyerCheck());
        //20190408
        order.setDeliveryId(entity.getDeliveryId());
        order.setSalesareaId(entity.getSalesareaId());
        
        return order;
    }
    
    public OrderDetail orderDetailTransfor(EcOrderDetail entity){
        OrderDetail vo = new OrderDetail();
        vo.setId(entity.getId());
        if(entity.getProduct()!=null){
            vo.setProduct(this.productTransfor(entity.getProduct()));
        }
        vo.setTotal(entity.getTotal());
        vo.setPrice(entity.getPrice());
        vo.setQuantity(entity.getQuantity());
        vo.setSno(entity.getSno());
        vo.setShipping(entity.getShipping());

        return vo;
    }
    
    public OrderMessage orderMessageTransfor(EcOrderMessage entity){
        OrderMessage vo = new OrderMessage();
        vo.setId(entity.getId());
        vo.setMessage(entity.getMessage());
        vo.setCreator(entity.getCreator().getLoginAccount());
//        vo.setCreatetime(entity.getCreatetime());
        vo.setCreatetime(sdf.format(entity.getCreatetime()));
        if(entity.getReadtime()!=null){
            vo.setReadtime(sdf.format(entity.getReadtime()));
        }
        vo.setBuyer(entity.isBuyer());
        
        return vo;
    }
    
    public Product productTransfor(EcProduct entity){
        Product vo = new Product();
        vo.setId(entity.getId());
        if(entity.getStore()!=null){
            vo.setStore(this.storeTransfor(entity.getStore()));
        }
        vo.setCname(entity.getCname());
        vo.setCode(entity.getCode());
        vo.setPrice(entity.getPrice());
        if(entity.getType()!=null){
            vo.setType(this.prdTypeTransfor(entity.getType()));
        }
        vo.setStatus(entity.getStatus());
        vo.setDisabled(entity.isDisabled());
        if(entity.getPriceUnit()!=null){//計價單位
            EcOption option = ecOptionFacade.find(entity.getPriceUnit());
            if(option!=null){
                vo.setPrdUnit(option.getCname());
            }
        }
        
        //商品照片
        String url = jndiConfig.getProperty("url.prefix");
        List<String> imageList = new ArrayList<>();
        String appUrl = url + "/ec-service/resources/image?oid=";
        if(entity.getCoverPicId()!=null){
            //商品封面照片
            EcFile file = fileFacade.find(entity.getCoverPicId());
            String imageUrl = appUrl + file.getId();
            imageList.add(imageUrl);
        }else{
            List<EcFile> fileList = ecProductFacade.findImageByProduct(entity);
            if(CollectionUtils.isNotEmpty(fileList)){
                //String appUrl = "/ec"; // 手機自行補 http://host_or_ip/
                //String imageUrl = appUrl + "/service/image?oid=" + file.getId();
                for(EcFile file:fileList){
                    String imageUrl = appUrl + file.getId();
                    imageList.add(imageUrl);
                }
            }
        }
        vo.setImageList(imageList);

        return vo;
    }
    
    public Store storeTransfor(EcStore entity){
        Store vo = new Store();
        vo.setId(entity.getId());
        if(entity.getSeller()!=null){
            vo.setSeller(this.sellerTransfor(entity.getSeller()));
        }
        vo.setCname(entity.getCname());
        vo.setEname(entity.getEname());
        vo.setBrief(entity.getBrief());
        vo.setDisabled(entity.isDisabled());
        vo.setOpened(entity.getOpened());
        vo = ecOrderRateFacade.countRateByStore(vo);

        return vo;
    }
    
    public CusAddr cusAddrTransfor(EcCusAddr entity){
        CusAddr vo = new CusAddr();
        vo.setId(entity.getId());
        vo.setAlias(entity.getAlias());
        vo.setAddress(entity.getAddress());
        vo.setPhone(entity.getPhone());
        vo.setPrimary(entity.isPrimary());
        vo.setCarNo(entity.getCarNo());
        vo.setPatrolLatitude(entity.getPatrolLatitude());
        vo.setPatrolLongitude(entity.getPatrolLongitude());
        if(entity.getDeliveryId() !=null){
            vo.setDeliveryId(entity.getDeliveryId());
            List<DeliveryPlace> list = deliveryFacade.findById(entity.getDeliveryId());
            if(CollectionUtils.isNotEmpty(list)){
                String deliveryName = list.get(0).getName();
                vo.setDeliveryName(deliveryName);
            }
        }
        
        return vo;
    }
    
    public PrdType prdTypeTransfor(EcPrdType entity){
        PrdType vo = new PrdType();
        vo.setId(entity.getId());
        vo.setCname(entity.getCname());
        vo.setEname(entity.getEname());
        vo.setCode(entity.getCode());
        vo.setMemo(entity.getMemo());
        if(entity.getParent()!=null){
            vo.setParentId(entity.getParent().getId());
        }
        vo.setLevelnum(entity.getLevelnum());

        return vo;
    }
    
    public Shipping shippingTransfor(EcShipping entity){
        Shipping vo = new Shipping();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setType(entity.getType());

        return vo;
    }
    
    public OrderShipInfo orderShipInfoTransfor(EcOrderShipInfo entity){
        OrderShipInfo vo = new OrderShipInfo();
        vo.setId(entity.getId());
        vo.setRecipient(entity.getRecipient());
        vo.setPhone(entity.getPhone());
        vo.setAddress(entity.getAddress());
        vo.setCarNo(entity.getCarNo());
        vo.setDriver(entity.getDriver());
        vo.setPatrolLatitude(entity.getPatrolLatitude());
        vo.setPatrolLongitude(entity.getPatrolLongitude());

        return vo;
    }
    
    public Payment paymentTransfor(EcPayment entity){
        Payment vo = new Payment();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setMemo(entity.getMemo());
        vo.setType(entity.getType());
        vo.setSortnum(entity.getSortnum());

        return vo;
    }
    
    public MemberMsg memberMsgTransfor(EcMemberMsg entity){
        MemberMsg vo = new MemberMsg();
        vo.setId(entity.getId());
        if(entity.getMember()!=null){
            vo.setMemberId(entity.getMember().getId());
        }
        if(entity.getProduct()!=null){
            vo.setPrdId(entity.getProduct().getId());
        }
        if(entity.getStore()!=null){
            vo.setStoreId(entity.getStore().getId());
        }
        vo.setMessage(entity.getMessage());
        vo.setCreator(entity.getCreator().getLoginAccount());
//        vo.setCreatetime(entity.getCreatetime());
        vo.setCreatetime(sdf.format(entity.getCreatetime()));

        return vo;
    }
    
    public Company companyTransfor(EcCompany entity){
        Company vo = new Company();
        vo.setId(entity.getId());
        vo.setCname(entity.getCname());
        vo.setIdCode(entity.getIdCode());
        vo.setTel1(entity.getTel1());
        vo.setOwner1(entity.getOwner1());
        if(entity.getState()!=null){
            vo.setState(entity.getState());
//            vo.setSalesArea(ecOptionFacade.find(entity.getState()).getCname());
            EcOption option = ecOptionFacade.find(entity.getState());
            if(option!=null){
                vo.setSalesArea(option.getCname());
            }
        }
        if(entity.getStartAt()!=null){
            vo.setStartAt(sdf.format(entity.getStartAt()));
        }
        if(entity.getCategory()!=null){
            vo.setCategory(entity.getCategory());
            EcOption ecOption = ecOptionFacade.find(entity.getCategory());
            if(ecOption!=null){
                vo.setIndustry(ecOption.getCname());
            }
        }
        vo.setCapital(entity.getCapital());
        vo.setYearIncome(entity.getYearIncome());
        
        return vo;
    }
    
}
