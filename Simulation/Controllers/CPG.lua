-- DO NOT WRITE CODE OUTSIDE OF THE if-then-end SECTIONS BELOW!! (unless the code is a function definition)



if (sim_call_type==sim_childscriptcall_initialization) then
    require("lua/modular/CPG")
    
	robot=simGetObjectHandle('North')
    Motor=simGetObjectHandle('centralMotor') 
    SensorA=simGetObjectHandle('psensorA')
    SensorB=simGetObjectHandle('psensorB')
    SensorC=simGetObjectHandle('psensorC')
    SensorD=simGetObjectHandle('psensorD')
    
    suffix= simGetNameSuffix(nil)
    myhandle = simGetScriptHandle(nil)

    connh = {}
    recdata = {}
    
    if(simGetStringSignal('ModHandles')==nil) then
        index = -1
    else
        data = simGetStringSignal('ModHandles')
        modhandles = simUnpackInt32Table(data)
        index = -1
        for i=1,#modhandles do
            if (robot==modhandles[i]) then
                index = i
            end
        end
        --print(index..' '..modhandles[index])
    end

    if(simGetStringSignal('ConnHandles')==nil) then
        connh = {-1,-1,-1,-1}
    else
        data = simGetStringSignal('ConnHandles')
        conh = simUnpackInt32Table(data,4*(index-1),4)
        for i=1,#conh do
            if (conh[i] ~= -1) then
                connh[i] = simGetScriptAssociatedWithObject(conh[i])
            else
                connh[i] = -1
            end
        end
        --print(index)
        --for k,v in pairs(connh) do print(k,v) end    
    end
   


    teta = {0,-1,-1,-1,-1}
    ampli = 0
    dampli = 0
    offset= 0
    doffset = 0
    dt = 0.05
    pfactor = 0.3
    v = 0.7
    wij = {}
    wij[1]= 7
    wij[2]= 7
    wij[3]= 7
    wij[4]= 7

    if(simGetStringSignal('ControlParam')==nil) then
        ampd = 0.5
        offd = 0
        phasediff = {}
        phasediff[1] = math.pi*pfactor
        phasediff[2]= math.pi*pfactor
        phasediff[3] = math.pi*pfactor
        phasediff[4]= math.pi*pfactor
    else
        data = simGetStringSignal('ControlParam')
        param = simUnpackFloatTable(data,6*(index-1),6)
        print(index)
        --for k,v in pairs(param) do print(k,v) end
        ampd = param[1]
        offd = param[2]
        phasediff = {}
        phasediff[1] = param[3]
        phasediff[2] = param[4]
        phasediff[3] = param[5]
        phasediff[4] = param[6]
    end
    


end


if (sim_call_type==sim_childscriptcall_actuation) then

    --print('********* '.. suffix)
    --for k,v in pairs(teta) do print(k,v) end
    --UpdateCPG
    
    teta[1],ampli,dampli,offset,doffset = updateCPG(teta,ampli,dampli,offset,doffset,dt,ampd,offd,phasediff,v,wij)

    angle = (math.pi/2)*(offset+(math.cos(teta[1])*ampli))
    simSetJointTargetPosition(Motor,angle)

    for i=1,#connh do
        if (connh[i]~=-1) then
            simSendData(connh[i],1038,'phase',teta[1])
            --print('Sent phase data from '..myhandle..' to '..connh[i])
        end
    end

end


if (sim_call_type==sim_childscriptcall_sensing) then
    for k in pairs(recdata) do recdata[k]=nil end
    for i=2,#teta do teta[i]=-1 end

    data,sender = simReceiveData(1038,'phase')
    while (data ~= nil) do
        recdata[sender] = data
        --print('Received phase data from '..sender..' in '..myhandle)
        data,sender = simReceiveData(1038,'phase')
        
    end

    for k in pairs(recdata) do
        for i=1,#connh do
            if (connh[i]==k) then
            teta[i+1]=recdata[k]
            end
        end
    end

end


if (sim_call_type==sim_childscriptcall_cleanup) then

	-- Put some restoration code here

end
