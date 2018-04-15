function getTPoints(mseq,Width,initangle)
    local TPoints={}
    local angle = initangle
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-2*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+2*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 2 --Distance added by the current part
            TPoints[i][8] = 's'

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-4*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+4*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 4 --Distance added by the current part
        TPoints[i][8] = 'b'

        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         TPoints[i][7] = xo + yo
         TPoints[i][8] = 'r'

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        TPoints[i][7] = -xo + yo
        TPoints[i][8] = 'l'
        end
    end
    return TPoints
end

function getDistance(CurrentTPart,TPoints,seqlength,position,initangle)
    local Goal = false
    local Dxo = 0
    local Dyo = 0
    local sum = 0
    local D = 0
    local Dout = 0

    if(CurrentTPart >= 1) then
               
        if(CurrentTPart<=seqlength) then
	
	-- Check if over output point taking into account output angle
            if(TPoints[CurrentTPart][6] == 0) then
                if(position[2]>TPoints[CurrentTPart][4]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == math.pi/2) then
                if(position[1]<TPoints[CurrentTPart][3]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == -math.pi/2) then
                if(position[1]>TPoints[CurrentTPart][3]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == math.pi or TPoints[CurrentTPart][6] == -math.pi) then
                if(position[2]<TPoints[CurrentTPart][4]) then
                    CurrentTPart = CurrentTPart + 1
                end
            end

	end

	if(CurrentTPart<=seqlength) then

            -- Check if over input point taking into account input angle
            if(TPoints[CurrentTPart][5] == 0) then
                if(position[2]<TPoints[CurrentTPart][2]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == math.pi/2) then
                if(position[1]>TPoints[CurrentTPart][1]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == -math.pi/2) then
                if(position[1]<TPoints[CurrentTPart][1]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == math.pi or TPoints[CurrentTPart][5] == -math.pi) then
                if(position[2]>TPoints[CurrentTPart][2]) then
                    CurrentTPart = CurrentTPart - 1
                end
            end
        end

	-- Check if robot got out of the last part
        if(CurrentTPart>seqlength) then
            Goal = true
        end

    else
	
	if(initangle == 0) then
        	if(position[2]>=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
	elseif(initangle == math.pi or initangle == -math.pi) then
		if(position[2]<=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
	elseif(initangle == math.pi/2) then
		if(position[1]<=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
	elseif(initangle == -math.pi/2) then
		if(position[1]>=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
    	end

    end	
        
    if(CurrentTPart>=1) then
        if(CurrentTPart<=#mseq) then

            Do = GetPartManhattanDistance(TPoints,CurrentTPart,position)
            --Dxo =  TPoints[CurrentTPart][3]-position[1]
            --Dyo =  TPoints[CurrentTPart][4]-position[2]
            --Do = math.sqrt((Dxo*Dxo)+(Dyo*Dyo))
            
            sum = 0
            for i=CurrentTPart+1,#mseq,1 do
                sum = sum + TPoints[i][7]
            end
            D = Do + sum
            --print(Do,sum,D)
            --print(D)
        end
    else
        Dxo =  0-position[1]
        Dyo =  0-position[2]
        --Do = math.sqrt((Dxo*Dxo)+(Dyo*Dyo))
        Do = Dxo + Dyo
            
        sum = 0
        for i=1,#mseq,1 do
            sum = sum + TPoints[i][7]
        end
        --print(sum)
        D = Do + sum
        --print(D)
    end
    -- Normalize Distance
    sum = 0
    for i=1,seqlength,1 do
        sum = sum + TPoints[i][7]
    end
    Dout = D/sum
    if(Dout>1) then
        Dout = 1
    end
    --print(Dout)
    return CurrentTPart,Goal,Dout
end


function getTPointsS(mseq,Width,initangle)
    local TPoints={}
    local angle = initangle
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-1*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+1*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 1 --Distance added by the current part
            TPoints[i][8] = 's'

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-0.5*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+0.5*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 0.5 --Distance added by the current part
        TPoints[i][8] = 'b'

        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.125
            yo = (Width/2) + 0.2
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         --TPoints[i][7] = xo + yo
         TPoints[i][7] = xo + yo + (Width/2) --Distance added in Manhattan distance
         TPoints[i][8] = 'r'

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.125
            yo = (Width/2) + 0.2
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        --TPoints[i][7] = -xo + yo
         TPoints[i][7] = -xo + yo + (Width/2) --Distance added in Manhattan distance
        TPoints[i][8] = 'l'
        end
    end
    return TPoints
end


function GetPartManhattanDistance(TPoints,CurrentPart,position)

    if (TPoints[CurrentPart][8]=='s' or TPoints[CurrentPart][8] == 'b') then
        
            Dyo = math.abs(TPoints[CurrentPart][4]-position[2])
            Dxo = math.abs(TPoints[CurrentPart][3]-position[1])
            Do = Dxo+Dyo; 
            return Do       

    elseif (TPoints[CurrentPart][8]=='r' or TPoints[CurrentPart][8]=='l') then
        --Take into account output angle
        angle = TPoints[CurrentPart][6]
        outputX = TPoints[CurrentPart][3]
        outputY = TPoints[CurrentPart][4]
        inputX = TPoints[CurrentPart][1]
        inputY = TPoints[CurrentPart][2]
        --print(angle,outputX,outputY,inputX,inputY)
        if(angle == 0 or angle == math.pi or angle == -math.pi) then
            DyoinputLine = math.abs(inputY-position[2])
            DxoouputLine = math.abs(outputX-position[1])
            --print(angle,DyoinputLine,DxoouputLine)
            if (DyoinputLine<DxoouputLine) then
                outputLine = math.abs(outputY-inputY)
                rInputLine = math.abs(outputX-position[1])
                Do = outputLine + rInputLine + DyoinputLine
                --print(outputLine,rInputLine,DyoinputLine,Do)
            else
                routputLine = math.abs(outputY-position[2])
                Do = routputLine + DxoouputLine
                --print(routputLine,DxoouputLine,Do)
            end
            return Do
        elseif(angle == math.pi/2 or angle == -math.pi/2) then
            Dxoinputline = math.abs(inputX-position[1])
            Dyooutputline = math.abs(outputY-position[2])
            --print(angle,Dxoinputline,Dyooutputline)
            if (Dxoinputline<Dyooutputline) then
                outputLine = math.abs(outputX-inputX)
                rInputLine = math.abs(outputY-position[2])
                Do = outputLine + rInputLine + Dxoinputline 
            else
                routputLine = math.abs(outputX-position[1])
                Do = routputLine + Dyooutputline
            end
            return Do  
        end  
    end
    
end

